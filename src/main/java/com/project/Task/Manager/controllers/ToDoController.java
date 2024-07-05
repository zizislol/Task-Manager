package com.project.Task.Manager.controllers;
import java.util.Map;

import javax.naming.Binding;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.project.Task.Manager.models.ToDo;
import com.project.Task.Manager.repositories.ToDoRepository;

import jakarta.validation.Valid;

@Controller
public class ToDoController {

    private final ToDoRepository toDoRepository;

    public ToDoController(ToDoRepository toDoRepository){
        this.toDoRepository = toDoRepository;
    }

    @GetMapping("/")
    public ModelAndView list(){
        return new ModelAndView("todo/list", Map.of("todos", toDoRepository.findAll(Sort.by("deadline"))));
    }

    @GetMapping("/create")
    public ModelAndView create(){
        return new ModelAndView("todo/form", Map.of("todo", new ToDo()));
    }

    @PostMapping("/create")
    public String create(@Valid ToDo toDo, BindingResult result){
        if (result.hasErrors()) {
            return "todo/form";
        }
        toDoRepository.save(toDo);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id){
        var toDo = toDoRepository.findById(id);
        if (toDo.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("todo/form", Map.of("todo", toDo));
    }

    @PostMapping("/edit/{id}")
    public String edit(@Valid ToDo todo, BindingResult result){
        if (result.hasErrors()) {
            return "todo/form";
        }
        toDoRepository.save(todo);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
        var todo = toDoRepository.findById(id);
        if (todo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("todo/delete", Map.of("todo", todo.get()));
    }

    @PostMapping("/delete/{id}")
    public String delete(ToDo todo){
        toDoRepository.delete(todo);
        return "redirect:/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@PathVariable Long id){
        var optionalTodo = toDoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var todo = optionalTodo.get();
        todo.markHasFinished();
        toDoRepository.save(todo);
        return "redirect:/";
    }

}