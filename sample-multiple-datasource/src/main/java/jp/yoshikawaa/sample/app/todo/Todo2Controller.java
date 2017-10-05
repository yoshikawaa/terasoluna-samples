package jp.yoshikawaa.sample.app.todo;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.groups.Default;

import org.dozer.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import jp.yoshikawaa.sample.app.todo.TodoForm.TodoCreate;
import jp.yoshikawaa.sample.app.todo.TodoForm.TodoDelete;
import jp.yoshikawaa.sample.app.todo.TodoForm.TodoFinish;
import jp.yoshikawaa.sample.domain.model.Todo;
import jp.yoshikawaa.sample.domain.service.todo.ITodoService;

@Controller
@RequestMapping("todo2")
public class Todo2Controller extends TodoController {

    @Inject
    @Named("todo2Service")
    private ITodoService todoService;

    @Inject
    private Mapper beanMapper;
    
    @ModelAttribute
    public TodoForm setUpForm() {
        TodoForm form = new TodoForm();
        return form;
    }

    @RequestMapping(value = "list")
    public String list(Model model) {

        Collection<Todo> todos = todoService.findAll();
        model.addAttribute("todos", todos);
        return "todo/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Validated({ Default.class, TodoCreate.class }) TodoForm todoForm, BindingResult bindingResult,
            Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        Todo todo = beanMapper.map(todoForm, Todo.class);
        try {
            todoService.create(todo);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Created successfully!")));
        return "redirect:/todo2/list";
    }

    @RequestMapping(value = "finish", method = RequestMethod.POST)
    public String finish(@Validated({ Default.class, TodoFinish.class }) TodoForm form, BindingResult bindingResult,
            Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.finish(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Finished successfully!")));
        return "redirect:/todo2/list";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(@Validated({ Default.class, TodoDelete.class }) TodoForm form, BindingResult bindingResult,
            Model model, RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(model);
        }

        try {
            todoService.delete(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return list(model);
        }

        attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Deleted successfully!")));
        return "redirect:/todo2/list";
    }
}
