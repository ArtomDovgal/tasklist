package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.domain.exeption.ResourceNotFoundException;
import dev.dov.tasklist.domain.task.Status;
import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.domain.task.TaskImage;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.repository.TaskRepository;
import dev.dov.tasklist.repository.UserRepository;
import dev.dov.tasklist.service.ImageService;
import dev.dov.tasklist.service.TaskService;
import dev.dov.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(Long Id) {
        return taskRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long id) {
        return taskRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key="#task.id")
    public Task create(Task task, Long id) {
        User user = userService.getById(id);
        task.setStatus(Status.TODO);
        user.getTasks().add(task);
        userService.update(user);
        return task;
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key="#id")
    public void delete(Long id) {

        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict
    public void uploadImage(Long id, TaskImage image) {
        Task task = getById(id);
        String fileName = imageService.upload(image);
        task.getImages().add(fileName);
        taskRepository.save(task);
    }


}
