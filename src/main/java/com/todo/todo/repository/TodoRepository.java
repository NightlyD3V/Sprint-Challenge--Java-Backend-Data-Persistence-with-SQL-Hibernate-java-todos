package com.todo.todo.repository;

import com.todo.todo.model.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long>
{
    Todo todoid(long id);
}
