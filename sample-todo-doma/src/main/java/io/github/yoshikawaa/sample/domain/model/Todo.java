package io.github.yoshikawaa.sample.domain.model;

import java.io.Serializable;
import java.util.Date;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.SNAKE_LOWER_CASE)
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String todoId;

    private String todoTitle;

    private boolean finished;

    private Date createdAt;

    public Todo() {
    }
    
    public Todo(String todoId, String todoTitle, Date createdAt, boolean finished) {
        this.todoId = todoId;
        this.todoTitle = todoTitle;
        this.createdAt = createdAt;
        this.finished = finished;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
