package io.github.yoshikawaa.sample.app.welcome;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terasoluna.gfw.common.codelist.CodeList;

@RestController("/")
public class HelloController {

    @Inject
    @Named("CL_DEPARTMENT")
    private CodeList codeList;
    
    @GetMapping
    public String home() {
        return codeList.asMap().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(","));
    }

}
