package io.github.yoshikawaa.sample.app.pagination;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Data;

@Data
public class FindForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 10)
    private String todoTitle;
    private Boolean finished;
    @DateTimeFormat(iso = ISO.DATE)
    @Past
    private Date createdAt;
}
