package io.github.yoshikawaa.sample.app.pagination.session;

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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.terasoluna.gfw.web.el.Functions;

import io.github.yoshikawaa.sample.app.pagination.FindForm;
import io.github.yoshikawaa.sample.app.pagination.PageInfo;
import io.github.yoshikawaa.sample.domain.repository.TodoFindCondition;
import io.github.yoshikawaa.sample.domain.service.TodoService;

@Controller
@RequestMapping("/pagination/session")
@SessionAttributes({ "condition", "pageInfo" })
public class SessionBindFindController {

    @Autowired
    private Mapper mapper;

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String find(@Validated FindForm form, BindingResult bindingResult, @PageableDefault Pageable pageable,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "session/list";
        }

        TodoFindCondition condition = mapper.map(form, TodoFindCondition.class);
        model.addAttribute("page", todoService.findAllByCondition(condition, pageable));
        model.addAttribute("condition", condition);
        model.addAttribute("pageInfo", new PageInfo(pageable));
        return "session/list";
    }

    @GetMapping(params = "restore")
    public String restore(SessionStatus sessionStatus, @SessionAttribute("condition") TodoFindCondition condition,
            @SessionAttribute("pageInfo") PageInfo pageInfo) {

        sessionStatus.isComplete();

        return "redirect:/pagination/session?" + String.join("&", Functions.query(pageInfo), Functions.query(condition));
    }
}
