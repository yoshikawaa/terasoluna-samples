package io.github.yoshikawaa.sample.app.sample;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;

import io.github.yoshikawaa.gfw.test.support.WebAppContextMockMvcSupport;
import io.github.yoshikawaa.sample.app.common.RequestHeader;

public class SampleControllerTest extends WebAppContextMockMvcSupport {

    @Test
    public void test1() throws Exception {

        RequestHeader expected = new RequestHeader();
        expected.setUserAgent("FireFox");
        expected.setAccept("text/html");
        expected.setAcceptLanguage("ja-JP");
        expected.setAcceptEncoding("utf-8");

        mvc.perform(get("/sample").header("user-agent", "FireFox")
                .header("accept", "text/html")
                .header("accept-language", "ja-JP")
                .header("accept-encoding", "utf-8"))
                .andExpect(status().isOk())
                .andExpect(view().name("sample/home"))
                .andExpect(model().attribute("requestHeader", samePropertyValuesAs(expected)))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void test2() throws Exception {

        RequestHeader expected = new RequestHeader();
        expected.setUserAgent("Chrome");
        expected.setAccept("text/html");
        expected.setAcceptLanguage("ja-JP");
        expected.setAcceptEncoding("utf-8");

        mvc.perform(get("/sample").header("user-agent", "Chrome")
                .header("accept", "text/html")
                .header("accept-language", "ja-JP")
                .header("accept-encoding", "utf-8"))
                .andExpect(status().isOk())
                .andExpect(view().name("sample/home"))
                .andExpect(model().attribute("requestHeader", samePropertyValuesAs(expected)))
                .andExpect(model().attributeHasFieldErrorCode("requestHeader", "userAgent", "SupportedBrowser"));
    }

}
