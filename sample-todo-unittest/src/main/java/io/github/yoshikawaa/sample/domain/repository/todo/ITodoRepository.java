package io.github.yoshikawaa.sample.domain.repository.todo;

import java.util.Collection;

import io.github.yoshikawaa.sample.domain.model.Todo;

public interface ITodoRepository {
    Todo findOne(String todoId);

    Collection<Todo> findAll();

    void create(Todo todo);

    boolean update(Todo todo);

    void delete(Todo todo);

    long countByFinished(boolean finished);
}
