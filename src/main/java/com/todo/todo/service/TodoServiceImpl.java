package com.todo.todo.service;

import com.todo.todo.model.Todo;
import com.todo.todo.model.User;
import com.todo.todo.repository.RoleRepository;
import com.todo.todo.repository.TodoRepository;
import com.todo.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "todoService")
public class TodoServiceImpl implements TodoService
{
    @Autowired
    private TodoRepository todorepos;

    @Autowired
    private UserRepository userrepos;


    @Autowired
    private RoleRepository rolerepos;

    @Override
    public List<Todo> findAll()
    {
        List<Todo> list = new ArrayList<>();
        todorepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Todo findTodoById(long id)
    {
        return todorepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    @Override
    public void delete(long id)
    {
        if (todorepos.findById(id).isPresent())
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (todorepos.findById(id).get().getUser().getUsername().equalsIgnoreCase(authentication.getName()))
            {
                todorepos.deleteById(id);
            }
            else
            {
                throw new EntityNotFoundException(Long.toString(id) + " " + authentication.getName());
            }
        }
        else
        {
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public Todo save(Todo todo)
    {
        return todorepos.save(todo);
    }

    @Transactional
    @Override
    public Todo update(Todo todo, long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Todo currentUser = todorepos.todoid(todo.getTodoid());
        User user = userrepos.findByUsername(authentication.getName());

        if (currentUser != null)
        {
            if (id == currentUser.getTodoid())
            {
                if (todo.getDescription() != null)
                {
                    currentUser.setDescription(todo.getDescription());
                }

                if (todo.getDescription().length() > 0)
                {
                    for (Todo q : user.getTodos())
                    {
                        user.getTodos().add( new Todo(q.getDescription(), todo.getDate(), user));
                    }
                }
                return todorepos.save(currentUser);
            }
            else
            {
                throw new EntityNotFoundException(Long.toString(id) + " Not current user");
            }
        }
        else
        {
            throw new EntityNotFoundException(authentication.getName());
        }

    }
}
