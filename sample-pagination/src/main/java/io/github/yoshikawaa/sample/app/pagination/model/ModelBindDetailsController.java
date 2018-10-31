package io.github.yoshikawaa.sample.app.pagination.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import io.github.yoshikawaa.sample.domain.service.TodoService;

@Controller
@RequestMapping("/pagination/model/{todoId}")
public class ModelBindDetailsController {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    @Autowired
    private Mapper mapper;

    @Autowired
    private TodoService todoService;
    
    @GetMapping
    public String details(@PathVariable("todoId") String todoId, Model model, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, BindingResult ignoreResult2) {
        model.addAttribute("todo", todoService.findOne(todoId));
        return "model/details";
    }

    @PostMapping(params = "finish")
    @SuppressWarnings("unchecked")
    public String finish(@PathVariable("todoId") String todoId, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, BindingResult ignoreResult2, RedirectAttributes redirectAttributes) {

        todoService.finish(todoId);

        redirectAttributes.mergeAttributes(mapper.map(form, Map.class));
        redirectAttributes.addAttribute("createdAt", DATE_FORMAT.format(form.getCreatedAt()));
        redirectAttributes.mergeAttributes(mapper.map(pageInfo, Map.class));
        return "redirect:/pagination/model/{todoId}";
    }

    @PostMapping(params = "delete")
    @SuppressWarnings("unchecked")
    public String delete(@PathVariable("todoId") String todoId, FindForm form, BindingResult ignoreResult,
            PageInfo pageInfo, RedirectAttributes redirectAttributes) {

        todoService.delete(todoId);

        redirectAttributes.mergeAttributes(mapper.map(form, Map.class));
        redirectAttributes.addAttribute("createdAt", DATE_FORMAT.format(form.getCreatedAt()));
        redirectAttributes.mergeAttributes(mapper.map(pageInfo, Map.class));
        return "redirect:/pagination/model";
    }
}
