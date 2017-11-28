package io.github.yoshikawaa.sample.domain.service.todo;

import static io.github.yoshikawaa.sample.test.util.TestUtils.resultMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

import io.github.yoshikawaa.sample.domain.model.Todo;
import io.github.yoshikawaa.sample.domain.repository.todo.ITodoRepository;
import io.github.yoshikawaa.sample.domain.service.todo.TodoService;
import io.github.yoshikawaa.sample.test.support.MockitoRuleSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/test-context.xml" })
public class TodoServiceTest extends MockitoRuleSupport {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private ITodoRepository todoRepository;

    @Test
    public void testFindOne() throws Exception {
        // setup
        Todo todo = createTodo();

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(todo);

        // execute
        Todo result = todoRepository.findOne(todo.getTodoId());

        // assert
        assertThat(result).isNotNull().isEqualTo(todo);
    }

    @Test
    public void testFindOneNotFound() {
        // setup
        String todoId = "todo-001";

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(null);

        // execute and assert
        assertThatThrownBy(() -> {
            todoService.findOne(todoId);
        }).isInstanceOf(ResourceNotFoundException.class).hasMessage(resultMessage(StandardResultMessageType.ERROR,
                "[E404] The requested Todo is not found. (id=" + todoId + ")"));
    }

    @Test
    public void testFindAll() throws Exception {
        // setup
        Date createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-05");
        Collection<Todo> todos = new ArrayList<>();
        todos.add(new Todo("todo-001", "todo 001", createdAt, false));
        todos.add(new Todo("todo-002", "todo 002", createdAt, false));
        todos.add(new Todo("todo-003", "todo 003", createdAt, true));

        // mock setup
        when(todoRepository.findAll()).thenReturn(todos);

        // execute
        Collection<Todo> results = todoService.findAll();

        // assert
        assertThat(results).containsAll(todos);
    }

    @Test
    public void testCreate() throws Exception {
        // setup
        Todo todo = createTodo();
        
        // execute
        todoService.create(todo);

        // assert
        verify(todoRepository).create(argThat(new ArgumentMatcher<Todo>() {
            @Override
            public boolean matches(Object argument) {
                return argument.equals(todo);
            }
        }));
    }

    @Test
    public void testCreateOverMaxCount() throws Exception {
        // setup
        Todo todo = createTodo();
        
        // mock setup
        when(todoRepository.countByFinished(anyBoolean())).thenReturn(5L);

        // execute and assert
        assertThatThrownBy(() -> {
            todoService.create(todo);
        }).isInstanceOf(BusinessException.class).hasMessage(resultMessage(StandardResultMessageType.ERROR,
                "[E001] The count of un-finished Todo must not be over 5."));
    }

    @Test
    public void testFinish() throws Exception {
        // setup
        Todo todo = createTodo();

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(todo);

        // execute
        todoService.finish(todo.getTodoId());

        // assert
        verify(todoRepository).update(argThat(new ArgumentMatcher<Todo>() {
            @Override
            public boolean matches(Object argument) {
                return argument.equals(todo);
            }
        }));
    }

    @Test
    public void testFinishNotFound() {
        // setup
        String todoId = "todo-001";

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(null);

        // execute and assert
        assertThatThrownBy(() -> {
            todoService.finish(todoId);
        }).isInstanceOf(ResourceNotFoundException.class).hasMessage(resultMessage(StandardResultMessageType.ERROR,
                "[E404] The requested Todo is not found. (id=" + todoId + ")"));
    }

    @Test
    public void testFinishAlreadyFinished() throws Exception {
        // setup
        Todo todo = createTodo();
        todo.setFinished(true);
        String todoId = todo.getTodoId();

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(todo);

        // execute and assert
        assertThatThrownBy(() -> {
            todoService.finish(todoId);
        }).isInstanceOf(BusinessException.class).hasMessage(resultMessage(StandardResultMessageType.ERROR,
                "[E002] The requested Todo is already finished. (id=" + todoId + ")"));
    }

    @Test
    public void testDelete() throws Exception {
        // setup
        Todo todo = createTodo();

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(todo);

        // execute
        todoService.delete(todo.getTodoId());

        // assert
        verify(todoRepository).delete(argThat(new ArgumentMatcher<Todo>() {
            @Override
            public boolean matches(Object argument) {
                return argument.equals(todo);
            }
        }));
    }

    @Test
    public void testDeleteNotFound() {
        // setup
        String todoId = "todo-001";

        // mock setup
        when(todoRepository.findOne(anyString())).thenReturn(null);

        // execute and assert
        assertThatThrownBy(() -> {
            todoService.delete(todoId);
        }).isInstanceOf(ResourceNotFoundException.class).hasMessage(resultMessage(StandardResultMessageType.ERROR,
                "[E404] The requested Todo is not found. (id=" + todoId + ")"));
    }
    
    private Todo createTodo() throws Exception {
        String todoId = "todo-001";
        String todoTitle = "todo 001";
        Date createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-05");
        boolean finished = false;
        return new Todo(todoId, todoTitle, createdAt, finished);
    }

}
