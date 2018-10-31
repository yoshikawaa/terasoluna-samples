package io.github.yoshikawaa.sample.app.pagination.param;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.yoshikawaa.sample.app.pagination.FindForm;
import io.github.yoshikawaa.sample.domain.repository.TodoFindCondition;
import io.github.yoshikawaa.sample.domain.service.TodoService;

@Controller
@RequestMapping("/pagination/param")
public class ParamBindFindController {

    @Autowired
    private Mapper mapper;

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String find(@Validated FindForm form, BindingResult bindingResult, @PageableDefault Pageable pageable,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "param/list";
        }

        TodoFindCondition condition = mapper.map(form, TodoFindCondition.class);
        model.addAttribute("page", todoService.findAllByCondition(condition, pageable));
        return "param/list";
    }
}
