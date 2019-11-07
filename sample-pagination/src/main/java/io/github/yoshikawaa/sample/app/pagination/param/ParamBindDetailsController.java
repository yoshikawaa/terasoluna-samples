package io.github.yoshikawaa.sample.app.pagination.param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.web.el.Functions;

import io.github.yoshikawaa.sample.app.pagination.FindForm;
import io.github.yoshikawaa.sample.app.pagination.PageInfo;
import io.github.yoshikawaa.sample.domain.service.TodoService;

@Controller
@RequestMapping("/pagination/param/{todoId}")
public class ParamBindDetailsController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String details(@PathVariable("todoId") String todoId, Model model) {
        model.addAttribute("todo", todoService.findOne(todoId));
        return "param/details";
    }

    @PostMapping(params = "finish")
    public String finish(@PathVariable("todoId") String todoId, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, BindingResult ignoreResult2) {

        todoService.finish(todoId);

        return "redirect:/pagination/param/{todoId}?" + String.join("&", Functions.query(pageInfo), Functions.query(form));
    }

    @PostMapping(params = "delete")
    public String delete(@PathVariable("todoId") String todoId, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, BindingResult ignoreResult2) {

        todoService.delete(todoId);

        return "redirect:/pagination/param?" + String.join("&", Functions.query(pageInfo), Functions.query(form));
    }
}
