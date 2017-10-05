package jp.yoshikawaa.sample.domain.repository.todo;

import java.util.Collection;

import jp.yoshikawaa.sample.domain.model.Todo;
import jp.yoshikawaa.sample.domain.repository.annotation.DataSource2;

@DataSource2
public interface ITodo2Repository {

    Todo findOne(String todoId);

    Collection<Todo> findAll();

    void create(Todo todo);

    boolean update(Todo todo);

    void delete(Todo todo);

    long countByFinished(boolean finished);
}