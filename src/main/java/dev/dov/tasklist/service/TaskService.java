package dev.dov.tasklist.service;

import dev.dov.tasklist.domain.task.Task;

import java.util.List;

public interface TaskService {

    Task getById(Long Id);

    List<Task> getAllByUserId(Long id);

    Task create(Task task, Long id);

    Task update(Task task);

    void delete(Long id);
}
