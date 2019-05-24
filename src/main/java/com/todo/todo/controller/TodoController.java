package com.todo.todo.controller;

import com.todo.todo.model.Todo;
import com.todo.todo.model.User;
import com.todo.todo.service.TodoService;
import com.todo.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    TodoService todoService;
    UserService userService;

    @GetMapping(value = "users/mine",
            produces = {"application/json"})
    public ResponseEntity<?> userTodos()
    {
        List<Todo> allTodos = todoService.findAll();
        return new ResponseEntity<>(allTodos, HttpStatus.OK);
    }

    @PostMapping(value = "/users",
            produces = {"application/json"})
    public ResponseEntity<?> addUser(@Valid @RequestBody User newUser) throws URISyntaxException
    {
        newUser = userService.save(newUser);
        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userid}").buildAndExpand(newUser.getUserid()).toUri();
        responseHeaders.setLocation(newUserURI);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping(value = "/users/todo/{userid}",
            produces = {"application/json"})
    public ResponseEntity<?> addTodoById(@Valid @RequestBody Todo newTodo) throws URISyntaxException
    {
        newTodo = todoService.save(newTodo);
        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newTodoURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{todoid}").buildAndExpand(newTodo.getTodoid()).toUri();
        responseHeaders.setLocation(newTodoURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/todos/todoid/{todoid}", produces = {"application/json"})
    public ResponseEntity<?> updateTodoById(@PathVariable User user, long id)
    {
        userService.update(user,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "users/userid/{userid}", produces = {"application/json"})
    public ResponseEntity<?> deleteUser(@PathVariable long id)
    {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
