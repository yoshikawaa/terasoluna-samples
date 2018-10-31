package io.github.yoshikawaa.sample.domain.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import io.github.yoshikawaa.sample.domain.model.Todo;

public interface TodoRepository {

    List<Todo> findAllByCondition(@Param("condition") TodoFindCondition condition,
            @Param("pageable") Pageable pageable);

    long countByCondition(@Param("condition") TodoFindCondition condition);

    Todo findOne(String todoId);

    void create(Todo todo);

    boolean update(Todo todo);

    void delete(Todo todo);
}
