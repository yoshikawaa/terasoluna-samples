package jp.yoshikawaa.sample.app.todo;

import static jp.yoshikawaa.sample.test.util.TestUtils.buildCsrfToken;
import static jp.yoshikawaa.sample.test.util.TestUtils.resultMessage;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasToString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;
import org.terasoluna.gfw.web.logging.mdc.MDCClearFilter;
import org.terasoluna.gfw.web.logging.mdc.XTrackMDCPutFilter;

import jp.yoshikawaa.sample.domain.model.Todo;
import jp.yoshikawaa.sample.domain.service.todo.TodoService;
import jp.yoshikawaa.sample.test.support.MockitoRuleSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration({ "classpath*:META-INF/spring/applicationContext.xml",
                "classpath*:META-INF/spring/spring-security.xml" }),
        @ContextConfiguration("classpath*:META-INF/spring/spring-mvc.xml") })
public class TodoControllerWebApplicationTest extends MockitoRuleSupport {

    private MockMvc mvc;
    private MockHttpSession session;

    @Inject
    private WebApplicationContext context;
    @Inject
    @InjectMocks
    private TodoController todoController;
    @Mock
    private TodoService todoService;

    @Before
    public void setup() {
        // setup mock mvc
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new MDCClearFilter(), new DelegatingFilterProxy("exceptionLoggingFilter", context),
                        new XTrackMDCPutFilter(), new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity()).alwaysDo(log()).build();

        // setup mock session
        session = new MockHttpSession(context.getServletContext());
    }

    @Test
    public void testList() throws Exception {
        // setup
        Collection<Todo> todos = new ArrayList<>();

        // mock setup
        when(todoService.findAll()).thenReturn(todos);

        // execute and assert
        mvc.perform(list()).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().attribute("todos", todos));
    }

    @Test
    public void testCreate() throws Exception {
        // setup
        String todoTitle = "todo 001";

        // execute and assert
        mvc.perform(create(todoTitle)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.SUCCESS, "Created successfully!"))));

        verify(todoService).create(argThat(new ArgumentMatcher<Todo>() {
            @Override
            public boolean matches(Object argument) {
                return todoTitle.equals(((Todo) argument).getTodoTitle());
            }
        }));
    }

    @Test
    public void testCreateValidationError() throws Exception {
        // setup
        String todoTitle = null;

        // execute and assert
        mvc.perform(create(todoTitle)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoTitle", "NotNull"))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, nullValue()));

        verify(todoService, never()).create(any(Todo.class));
    }

    @Test
    public void testCreateValidationError2() throws Exception {
        // setup
        String todoTitle = "123456789012345678901234567890E";

        // execute and assert
        mvc.perform(create(todoTitle)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoTitle", "Size"))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, nullValue()));

        verify(todoService, never()).create(any(Todo.class));
    }

    @Test
    public void testCreateBusinessError() throws Exception {
        // setup
        String todoTitle = "todo 001";

        // mock setup
        when(todoService.create(any(Todo.class))).thenThrow(
                new BusinessException(ResultMessages.error().add(ResultMessage.fromText("mock:business error"))));

        // execute and assert
        mvc.perform(create(todoTitle)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.ERROR, "mock:business error"))));

        verify(todoService).create(any(Todo.class));
    }

    @Test
    public void testFinish() throws Exception {
        // setup
        String todoId = "todo-001";

        // execute and assert
        mvc.perform(finish(todoId)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.SUCCESS, "Finished successfully!"))));

        verify(todoService).finish(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return todoId.equals(argument);
            }
        }));
    }

    @Test
    public void testFinishValidationError() throws Exception {
        // setup
        String todoId = null;

        // execute and assert
        mvc.perform(finish(todoId)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoId", "NotNull"))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, nullValue()));

        verify(todoService, never()).finish(anyString());
    }

    @Test
    public void testFinishBusinessError() throws Exception {
        // setup
        String todoId = "todo-001";

        // mock setup
        when(todoService.finish(anyString())).thenThrow(
                new BusinessException(ResultMessages.error().add(ResultMessage.fromText("mock:business error"))));

        // execute and assert
        mvc.perform(finish(todoId)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.ERROR, "mock:business error"))));

        verify(todoService).finish(anyString());
    }

    @Test
    public void testDelete() throws Exception {
        // setup
        String todoId = "todo-001";

        // execute and assert
        mvc.perform(delete(todoId)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.SUCCESS, "Deleted successfully!"))));

        verify(todoService).delete(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return todoId.equals(argument);
            }
        }));
    }

    @Test
    public void testDeleteValidationError() throws Exception {
        // setup
        String todoId = null;

        // execute and assert
        mvc.perform(delete(todoId)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("todoForm", "todoId", "NotNull"))
                .andExpect(flash().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, nullValue()));

        verify(todoService, never()).delete((anyString()));
    }

    @Test
    public void testDeleteBusinessError() throws Exception {
        // setup
        String todoId = "todo-001";

        // mock setup
        doThrow(new BusinessException(ResultMessages.error().add(ResultMessage.fromText("mock:business error"))))
                .when(todoService).delete(anyString());

        // execute and assert
        mvc.perform(delete(todoId)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.ERROR, "mock:business error"))));

        verify(todoService).delete(anyString());
    }

    private RequestBuilder list() {
        return get("/todo/list");
    }

    private RequestBuilder create(String todoTitle) throws Exception {
        CsrfToken token = buildCsrfToken(mvc, session);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("todoTitle", todoTitle);
        params.add(token.getParameterName(), token.getToken());

        return post("/todo/create").session(session).params(params);
    }

    private RequestBuilder finish(String todoId) throws Exception {
        CsrfToken token = buildCsrfToken(mvc, session);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("todoId", todoId);
        params.add(token.getParameterName(), token.getToken());

        return post("/todo/finish").session(session).params(params);
    }

    private RequestBuilder delete(String todoId) throws Exception {
        CsrfToken token = buildCsrfToken(mvc, session);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("todoId", todoId);
        params.add(token.getParameterName(), token.getToken());

        return post("/todo/delete").session(session).params(params);
    }

}
