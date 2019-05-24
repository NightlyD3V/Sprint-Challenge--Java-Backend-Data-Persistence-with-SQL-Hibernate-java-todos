package com.todo.todo.service;

import com.todo.todo.model.Todo;
import java.util.List;

public interface TodoService
{
    List<Todo> findAll();

    Todo findQuoteById(long id);

    List<Todo> findByUserName (String username);

    void delete(long id);

    Todo save(Todo quote);
}
