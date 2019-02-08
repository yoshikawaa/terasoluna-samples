package io.github.yoshikawaa.sample.app.sample;

import java.util.Date;

import javax.validation.constraints.Future;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleForm {

    @DateTimeFormat(iso = ISO.DATE)
    @Future
    private Date date;
}
