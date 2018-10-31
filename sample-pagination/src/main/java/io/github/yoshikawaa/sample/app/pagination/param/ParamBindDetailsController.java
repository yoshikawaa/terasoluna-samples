package io.github.yoshikawaa.sample.app.pagination.param;

import java.util.Map;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.yoshikawaa.sample.app.pagination.FindForm;
import io.github.yoshikawaa.sample.app.pagination.PageInfo;
import io.github.yoshikawaa.sample.domain.repository.TodoFindCondition;
import io.github.yoshikawaa.sample.domain.service.TodoService;

@Controller
@RequestMapping("/pagination/param/{todoId}")
public class ParamBindDetailsController {

    @Autowired
    private Mapper mapper;

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String details(@PathVariable("todoId") String todoId, Model model) {
        model.addAttribute("todo", todoService.findOne(todoId));
        return "param/details";
    }

    @PostMapping(params = "finish")
    @SuppressWarnings("unchecked")
    public String finish(@PathVariable("todoId") String todoId, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, BindingResult ignoreResult2, RedirectAttributes redirectAttributes) {

        todoService.finish(todoId);

        redirectAttributes.mergeAttributes(mapper.map(mapper.map(form, TodoFindCondition.class), Map.class));
        redirectAttributes.mergeAttributes(mapper.map(pageInfo, Map.class));
        return "redirect:/pagination/param/{todoId}";
    }

    @PostMapping(params = "delete")
    @SuppressWarnings("unchecked")
    public String delete(@PathVariable("todoId") String todoId, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, BindingResult ignoreResult2, RedirectAttributes redirectAttributes) {

        todoService.delete(todoId);

        redirectAttributes.mergeAttributes(mapper.map(mapper.map(form, TodoFindCondition.class), Map.class));
        redirectAttributes.mergeAttributes(mapper.map(pageInfo, Map.class));
        return "redirect:/pagination/param";
    }
}
