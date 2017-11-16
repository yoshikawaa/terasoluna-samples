package jp.yoshikawaa.sample.test.web.servlet.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.terasoluna.gfw.web.token.TokenStringGenerator;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInfo;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenStore;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

public final class TerasolunaGfwMockMvcRequestPostProcessors {

    public static TransactionTokenRequestPostProcessor transaction() {
        return new TransactionTokenRequestPostProcessor();
    }

    public static TransactionTokenRequestPostProcessor transaction(String namespace) {
        return new TransactionTokenRequestPostProcessor(namespace);
    }

    public static TransactionTokenRequestPostProcessor transaction(String classTokenName, String methodTokenName) {
        return new TransactionTokenRequestPostProcessor(classTokenName, methodTokenName);
    }

    public static class TransactionTokenRequestPostProcessor implements RequestPostProcessor {

        private static final String GLOBAL_TOKEN_NAME = "globalToken";

        private final String namespace;
        private boolean useInvalidToken;

        private TransactionTokenRequestPostProcessor() {
            this.namespace = GLOBAL_TOKEN_NAME;
        }

        private TransactionTokenRequestPostProcessor(String namespace) {
            this.namespace = namespace;
        }

        private TransactionTokenRequestPostProcessor(String classTokenName, String methodTokenName) {
            this.namespace = createTokenName(classTokenName, methodTokenName);
        }

        public TransactionTokenRequestPostProcessor useInvalidToken() {
            this.useInvalidToken = true;
            return this;
        }

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {

            TransactionTokenInterceptor interceptor = getInterceptor(request);
            TransactionTokenStore tokenStore = setupTokenStore(interceptor);
            TokenStringGenerator generator = getGenerator(interceptor);
            TransactionToken token = createToken(tokenStore, generator);

            String tokenValue = this.useInvalidToken ? "invalid" + token.getTokenString() : token.getTokenString();
            request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, tokenValue);

            return request;
        }

        private String createTokenName(final String classTokenName, final String methodTokenName) {

            StringBuilder tokenNameStringBuilder = new StringBuilder();
            if (classTokenName != null && !classTokenName.isEmpty()) {
                tokenNameStringBuilder.append(classTokenName);
            }
            if (methodTokenName != null && !methodTokenName.isEmpty()) {
                if (tokenNameStringBuilder.length() != 0) {
                    tokenNameStringBuilder.append("/");
                }
                tokenNameStringBuilder.append(methodTokenName);
            }
            if (tokenNameStringBuilder.length() == 0) {
                tokenNameStringBuilder.append(GLOBAL_TOKEN_NAME);
            }

            return tokenNameStringBuilder.toString();
        }

        private TransactionTokenInterceptor getInterceptor(MockHttpServletRequest request) {

            WebApplicationContext context = WebApplicationContextUtils
                    .getWebApplicationContext(request.getServletContext(), FrameworkServlet.SERVLET_CONTEXT_PREFIX);
            for (Entry<String, MappedInterceptor> mapped : context.getBeansOfType(MappedInterceptor.class).entrySet()) {
                HandlerInterceptor interceptor = mapped.getValue().getInterceptor();
                if (interceptor instanceof TransactionTokenInterceptor) {
                    return (TransactionTokenInterceptor) interceptor;
                }
            }

            throw new IllegalStateException("TransactionTokenInterceptor not found.");
        }

        private TokenStringGenerator getGenerator(TransactionTokenInterceptor interceptor) {
            return (TokenStringGenerator) ReflectionTestUtils.getField(interceptor, "generator");
        }

        private TransactionTokenStore setupTokenStore(TransactionTokenInterceptor interceptor) {

            TransactionTokenStore actualTokenStore = (TransactionTokenStore) ReflectionTestUtils.getField(interceptor,
                    "tokenStore");
            if (actualTokenStore instanceof TestTransactionTokenStore) {
                return actualTokenStore;
            }

            int transactionTokensPerTokenName = (int) ReflectionTestUtils.getField(actualTokenStore,
                    "transactionTokensPerTokenName");
            TransactionTokenStore tokenStore = new TestTransactionTokenStore(transactionTokensPerTokenName);
            ReflectionTestUtils.setField(interceptor, "tokenStore", tokenStore);

            return tokenStore;
        }

        private TransactionToken createToken(TransactionTokenStore tokenStore, TokenStringGenerator generator) {
            TransactionTokenInfo tokenInfo = new TransactionTokenInfo(namespace, TransactionTokenType.BEGIN);
            String tokenKey = tokenStore.createAndReserveTokenKey(tokenInfo.getTokenName());
            TransactionToken token = new TransactionToken(tokenInfo.getTokenName(), tokenKey,
                    generator.generate(UUID.randomUUID().toString()));
            tokenStore.store(token);

            return token;
        }

        private static class TestTransactionTokenStore implements TransactionTokenStore {

            public static final int NO_OF_TOKENS_PER_TOKEN_NAME = 10;

            private final String TOKEN_STRING_SEPARATOR = "~";

            private final int transactionTokensPerTokenName;
            private final Map<String, TokenHolder> store = new HashMap<>();

            private TestTransactionTokenStore() {
                this.transactionTokensPerTokenName = NO_OF_TOKENS_PER_TOKEN_NAME;
            }

            private TestTransactionTokenStore(int transactionTokensPerTokenName) {
                this.transactionTokensPerTokenName = transactionTokensPerTokenName;
            }

            @Override
            public String getAndClear(TransactionToken token) {
                String key = createStoreKey(token);
                if (store.containsKey(key)) {
                    TokenHolder holder = store.get(key);
                    store.remove(key);
                    return holder.getToken();
                }
                return null;
            }

            @Override
            public void remove(TransactionToken token) {
                String key = createStoreKey(token);
                store.remove(key);
            }

            @Override
            public String createAndReserveTokenKey(String tokenName) {
                Map<String, TokenHolder> filteredByTokenName = new HashMap<>();
                for (Entry<String, TokenHolder> entry : store.entrySet()) {
                    if (tokenName.equals(entry.getKey().split(TOKEN_STRING_SEPARATOR)[0])) {
                        filteredByTokenName.put(entry.getKey(), entry.getValue());
                    }
                }
                if (filteredByTokenName.size() > transactionTokensPerTokenName) {
                    store.remove(getOldestKey(filteredByTokenName));
                }

                return UUID.randomUUID().toString();
            }

            @Override
            public void store(TransactionToken token) {
                String key = createStoreKey(token);
                store.put(key, new TokenHolder(token.getTokenValue(), System.currentTimeMillis()));
            }

            private String createStoreKey(TransactionToken token) {
                if (token == null) {
                    throw new IllegalArgumentException("token must not be null");
                }
                return token.getTokenName() + TOKEN_STRING_SEPARATOR + token.getTokenKey();
            }

            private String getOldestKey(Map<String, TokenHolder> holders) {
                long oldestTimestamp = Long.MAX_VALUE;
                String oldestKey = null;
                for (Entry<String, TokenHolder> holder : holders.entrySet()) {
                    long timestamp = holder.getValue().getTimestamp();
                    if (oldestTimestamp > timestamp) {
                        oldestTimestamp = timestamp;
                        oldestKey = holder.getKey();
                    }
                }
                return oldestKey;
            }

            private static final class TokenHolder implements Serializable {

                private static final long serialVersionUID = 1L;

                private final String token;

                private final long timestamp;

                public TokenHolder(final String token, final long timestamp) {
                    this.token = token;
                    this.timestamp = timestamp;
                }

                public String getToken() {
                    return token;
                }

                public long getTimestamp() {
                    return timestamp;
                }
            }
        }
    }
    
    private TerasolunaGfwMockMvcRequestPostProcessors() {
    }

}
