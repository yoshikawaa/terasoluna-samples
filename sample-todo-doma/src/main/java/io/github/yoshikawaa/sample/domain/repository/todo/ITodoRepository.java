package io.github.yoshikawaa.sample.domain.repository.todo;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import io.github.yoshikawaa.domain.repository.DomaRepository;
import io.github.yoshikawaa.sample.domain.model.Todo;

@Dao
@DomaRepository
public interface ITodoRepository {
    @Select
    Todo findOne(String todoId);
    @Select
    List<Todo> findAll();
    @Insert
    int create(Todo todo);
    @Update
    int update(Todo todo);
    @Delete
    int delete(Todo todo);
    @Select
    long countByFinished(boolean finished);
}
