package io.github.yoshikawaa.sample.test.web.servlet.result;

import static org.junit.Assert.fail;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

public class ResultMessagesResultMatchers {

    protected ResultMessagesResultMatchers() {
    }

    public <T> ResultMatcher type(final StandardResultMessageType type) {
        if (type == null) {
            fail("ResultMessages type must not be null.");
        }

        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages messages = getResultMessages(result);
                assertTrue("ResultMessages type is not'" + type + "'", type.equals(messages.getType()));
            }
        };
    }

    public <T> ResultMatcher codeExists(final String... codes) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages messages = getResultMessages(result);
                code: for (String code : codes) {
                    for (ResultMessage message : messages) {
                        if (code.equals(message.getCode())) {
                            break code;
                        }
                    }
                    fail("ResultMessage code '" + code + "' does not exist.");
                }
            }
        };
    }

    public <T> ResultMatcher textExists(final String... texts) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages messages = getResultMessages(result);
                code: for (String text : texts) {
                    for (ResultMessage message : messages) {
                        if (text.equals(message.getText())) {
                            break code;
                        }
                    }
                    fail("ResultMessage text '" + text + "' does not exist.");
                }
            }
        };
    }

    private String getResultMessagesAttribute(MvcResult result) {
        return ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;
        
//        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(
//                result.getRequest().getServletContext(), FrameworkServlet.SERVLET_CONTEXT_PREFIX);
//
//        for (String name : context.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }
//        
//        Object resolver = context.getBean("systemExceptionResolver");
//        return (String) ReflectionTestUtils.getField(resolver, "resultMessagesAttribute");
    }

    private ResultMessages getResultMessages(MvcResult result) {
        return (ResultMessages) result.getFlashMap().get(getResultMessagesAttribute(result));
    }

}
