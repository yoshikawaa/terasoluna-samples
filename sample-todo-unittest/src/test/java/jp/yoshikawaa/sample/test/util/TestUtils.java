package jp.yoshikawaa.sample.test.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

public class TestUtils {

    public static CsrfToken buildCsrfToken(MockMvc mvc, MockHttpSession session) throws Exception {
        MvcResult result = mvc.perform(get("/").session(session)).andExpect(status().isOk()).andReturn();
        return (CsrfToken) result.getRequest().getAttribute("_csrf");
    }

    public static String resultMessage(StandardResultMessageType type, String text) {
        return "ResultMessages [type=" + type + ", list=[ResultMessage [code=null, args=[], text=" + text + "]]]";
    }

}
