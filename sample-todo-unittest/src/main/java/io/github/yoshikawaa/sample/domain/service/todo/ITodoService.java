package io.github.yoshikawaa.sample.domain.service.todo;

import java.util.Collection;

import io.github.yoshikawaa.sample.domain.model.Todo;

public interface ITodoService {
    Collection<Todo> findAll();

    Todo create(Todo todo);

    Todo finish(String todoId);

    void delete(String todoId);
}
