package com.todo.todo.service;

import com.todo.todo.model.Todo;

import java.util.List;

public interface TodoService
{
    List<Todo> findAll();

    Todo findTodoById(long id);

    void delete(long id);

    Todo save(Todo quote);

    Todo update(Todo quote, long id);
}
