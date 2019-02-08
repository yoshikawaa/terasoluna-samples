package io.github.yoshikawaa.sample.app.sample;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class SampleController {

    @GetMapping
    public String index(SampleForm form) {
        return "sample/home";
    }
    
    @PostMapping
    public String validate(@Validated SampleForm form, BindingResult result) {
        return "sample/home";
    }
}
