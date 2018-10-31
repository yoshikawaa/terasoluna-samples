package io.github.yoshikawaa.sample.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.yoshikawaa.sample.domain.model.Todo;
import io.github.yoshikawaa.sample.domain.repository.TodoFindCondition;

public interface TodoService {

    Page<Todo> findAllByCondition(TodoFindCondition condition, Pageable pageable);

    Todo findOne(String todoId);

    Todo create(Todo todo);

    Todo finish(String todoId);

    void delete(String todoId);
}
