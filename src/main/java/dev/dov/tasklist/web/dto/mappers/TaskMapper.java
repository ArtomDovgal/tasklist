package dev.dov.tasklist.web.dto.mappers;

import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
