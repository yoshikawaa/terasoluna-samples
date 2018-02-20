package io.github.yoshikawaa.sample.app.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.yoshikawaa.sample.app.common.RequestHeader;

@Controller
@RequestMapping("sample")
public class SampleController {

    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @GetMapping
    public String handle(@Validated RequestHeader header, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "sample/home";
        }
        
        logger.info("model attribute's request header : {}", model.asMap().get("requestHeader"));

        return "sample/home";
    }
}
