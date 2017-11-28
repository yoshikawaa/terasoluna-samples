package io.github.yoshikawaa.sample.domain.repository.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import io.github.yoshikawaa.sample.domain.model.Todo;
import io.github.yoshikawaa.sample.domain.repository.todo.ITodoRepository;
import io.github.yoshikawaa.sample.test.helper.JdbcTemplateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/test-context.xml" })
@Transactional
@Sql(scripts = "classpath:META-INF/sql/todo-repository-before.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:META-INF/sql/todo-repository-after.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class TodoRepositoryTest {

    @Inject
    private ITodoRepository todoRepository;
    private JdbcTemplateHelper<Todo> jdbcTemplateHelper;

    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    
    @Before
    public void setup() {
        jdbcTemplateHelper = new JdbcTemplateHelper<>(jdbcTemplate, new TodoRowMapper(), "todo", "todo_id");
    }
    
    @Test
    public void testFindOne() throws Exception {
        // setup
        String todoId = "todo-001";
        String todoTitle = "todo 001";
        Date createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-05");
        boolean finished = false;
        Todo todo = new Todo(todoId, todoTitle, createdAt, finished);

        // execute
        Todo result = todoRepository.findOne(todoId);

        // assert
        assertThat(result).isNotNull().isEqualToComparingFieldByField(todo);
    }

    @Test
    public void testFindAll() throws Exception {
        // setup
        Date createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-05");
        Collection<Todo> todos = new ArrayList<>();
        todos.add(new Todo("todo-001", "todo 001", createdAt, false));
        todos.add(new Todo("todo-002", "todo 002", createdAt, false));
        todos.add(new Todo("todo-003", "todo 003", createdAt, true));

        // execute
        Collection<Todo> results = todoRepository.findAll();

        // assert
        assertThat(results).hasSize(3);
        for (Todo result : results) {
            assertThat(result).isEqualToComparingFieldByField(
                    todos.stream().filter(t -> t.getTodoId().equals(result.getTodoId())).findFirst().get());
        }
    }

    @Test
    public void testCreate() throws Exception {
        // setup
        String todoId = UUID.randomUUID().toString();
        String todoTitle = "created todo";
        Date createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-05");
        boolean finished = false;
        Todo todo = new Todo(todoId, todoTitle, createdAt, finished);

        // execute
        todoRepository.create(todo);

        // assert
        Todo result = jdbcTemplateHelper.findOne(todoId);
        assertThat(result).isNotNull().isEqualToComparingFieldByField(todo);
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        String todoId = "todo-002";
        String todoTitle = "todo 002 updated";
        Date createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-06");
        boolean finished = true;
        Todo updated = new Todo(todoId, todoTitle, createdAt, finished);
        Todo todo = jdbcTemplateHelper.findOne(todoId);

        // execute
        todoRepository.update(updated);

        // assert
        Todo result = jdbcTemplateHelper.findOne(todoId);
        assertThat(result).isNotNull();
        assertThat(result.getTodoTitle()).isNotEqualTo(todo.getTodoTitle());
        assertThat(result.getCreatedAt()).isNotEqualTo(todo.getCreatedAt());
        assertThat(result.isFinished()).isNotEqualTo(todo.isFinished());
        assertThat(result).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void testDelete() throws Exception {
        // setup
        String todoId = "todo-003";
        Todo todo = jdbcTemplateHelper.findOne(todoId);

        // execute
        todoRepository.delete(todo);

        // assert
        assertThatThrownBy(() -> {
            jdbcTemplateHelper.findOne(todoId);
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void testCountByFinished() throws Exception {
        // execute
        long result = todoRepository.countByFinished(true);

        // assert
        assertThat(result).isEqualTo(1L);
    }

    private static class TodoRowMapper implements RowMapper<Todo> {
        @Override
        public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Todo(rs.getString("todo_id"), rs.getString("todo_title"), rs.getDate("created_at"),
                    rs.getBoolean("finished"));
        }
    }
}
