package dev.dov.tasklist.web.controller;

import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.service.TaskService;
import dev.dov.tasklist.web.dto.mappers.TaskMapper;
import dev.dov.tasklist.web.dto.task.TaskDto;
import dev.dov.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id")
    //@PreAuthorize("canAccessTask(#id)")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getById(@PathVariable Long id){
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Get TaskDto by id")
    //@PreAuthorize("canAccessTask(#id)")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteById(@PathVariable Long id){
        taskService.delete(id);
    }

    @PutMapping
    @Operation(summary = "Update task user")
    //@PreAuthorize("canAccessTask(#dto.id)")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto){
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }
}
