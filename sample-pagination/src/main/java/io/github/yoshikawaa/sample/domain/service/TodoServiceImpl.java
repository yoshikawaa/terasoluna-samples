package io.github.yoshikawaa.sample.domain.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import io.github.yoshikawaa.sample.domain.model.Todo;
import io.github.yoshikawaa.sample.domain.repository.TodoFindCondition;
import io.github.yoshikawaa.sample.domain.repository.TodoRepository;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    private JodaTimeDateFactory dateFactory;

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Page<Todo> findAllByCondition(TodoFindCondition condition, Pageable pageable) {

        long total = todoRepository.countByCondition(condition);
        List<Todo> todos = (total > 0) ? todoRepository.findAllByCondition(condition, pageable)
                : Collections.emptyList();
        return new PageImpl<Todo>(todos, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public Todo findOne(String todoId) {

        Todo todo = todoRepository.findOne(todoId);
        if (todo == null) {
            ResultMessages messages = ResultMessages.error()
                    .add(ResultMessage.fromText("[E404] The requested Todo is not found. (id=" + todoId + ")"));
            throw new ResourceNotFoundException(messages);
        }

        return todo;
    }

    @Override
    public Todo create(Todo todo) {

        todo.setTodoId(UUID.randomUUID().toString());
        todo.setFinished(false);
        todo.setCreatedAt(dateFactory.newDate());

        todoRepository.create(todo);
        return todo;
    }

    @Override
    public Todo finish(String todoId) {

        Todo todo = findOne(todoId);
        if (todo.isFinished()) {
            ResultMessages messages = ResultMessages.error()
                    .add(ResultMessage.fromText("[E002] The requested Todo is already finished. (id=" + todoId + ")"));
            throw new BusinessException(messages);
        }

        todo.setFinished(true);

        todoRepository.update(todo);
        return todo;
    }

    @Override
    public void delete(String todoId) {

        Todo todo = findOne(todoId);
        todoRepository.delete(todo);
    }

}
