package io.github.yoshikawaa.sample.domain.repository;

import java.io.Serializable;

import lombok.Data;

@Data
public class TodoFindCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    private String todoTitle;
    private Boolean finished;
    private String createdAt;
}
