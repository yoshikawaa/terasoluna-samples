package io.github.yoshikawaa.sample.app.todo;

import static io.github.yoshikawaa.sample.test.util.TestUtils.resultMessage;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasToString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

import io.github.yoshikawaa.sample.app.todo.TodoController;
import io.github.yoshikawaa.sample.domain.model.Todo;
import io.github.yoshikawaa.sample.domain.service.todo.TodoService;
import io.github.yoshikawaa.sample.test.support.MockitoRuleSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/test-context.xml")
public class TodoControllerStandaloneTest extends MockitoRuleSupport {

    private MockMvc mvc;

    @Mock
    private TodoService todoService;
    @Inject
    private Mapper beanMapper;

    @Before
    public void setup() {
        // setup mock mvc
        TodoController controller = new TodoController(beanMapper, todoService);
        mvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(log()).build();
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

        verify(todoService, never()).finish((any(String.class)));
    }

    @Test
    public void testFinishBusinessError() throws Exception {
        // setup
        String todoId = "todo-001";

        // mock setup
        when(todoService.finish(any(String.class))).thenThrow(
                new BusinessException(ResultMessages.error().add(ResultMessage.fromText("mock:business error"))));

        // execute and assert
        mvc.perform(finish(todoId)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.ERROR, "mock:business error"))));

        verify(todoService).finish(any(String.class));
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

        verify(todoService, never()).delete((any(String.class)));
    }

    @Test
    public void testDeleteBusinessError() throws Exception {
        // setup
        String todoId = "todo-001";

        // mock setup
        doThrow(new BusinessException(ResultMessages.error().add(ResultMessage.fromText("mock:business error"))))
                .when(todoService).delete(any(String.class));

        // execute and assert
        mvc.perform(delete(todoId)).andExpect(status().isOk()).andExpect(view().name("todo/list"))
                .andExpect(model().errorCount(0))
                .andExpect(model().attribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                        hasToString(resultMessage(StandardResultMessageType.ERROR, "mock:business error"))));

        verify(todoService).delete(any(String.class));
    }

    private RequestBuilder list() {
        return get("/todo/list");
    }

    private RequestBuilder create(String todoTitle) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("todoTitle", todoTitle);

        return post("/todo/create").params(params);
    }

    private RequestBuilder finish(String todoId) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("todoId", todoId);

        return post("/todo/finish").params(params);
    }

    private RequestBuilder delete(String todoId) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("todoId", todoId);

        return post("/todo/delete").params(params);
    }

}
