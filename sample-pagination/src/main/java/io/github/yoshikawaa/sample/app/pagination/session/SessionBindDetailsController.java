package io.github.yoshikawaa.sample.app.pagination.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.yoshikawaa.sample.domain.service.TodoService;

@Controller
@RequestMapping("/pagination/session/{todoId}")
public class SessionBindDetailsController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String details(@PathVariable("todoId") String todoId, Model model) {
        model.addAttribute("todo", todoService.findOne(todoId));
        return "session/details";
    }

    @PostMapping(params = "finish")
    public String finish(@PathVariable("todoId") String todoId) {

        todoService.finish(todoId);
        return "redirect:/pagination/session/{todoId}";
    }

    @PostMapping(params = "delete")
    public String delete(@PathVariable("todoId") String todoId) {

        todoService.delete(todoId);
        return "redirect:/pagination/session?restore";
    }
}
