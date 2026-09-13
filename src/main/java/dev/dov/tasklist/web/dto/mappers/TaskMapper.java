package dev.dov.tasklist.web.dto.mappers;

import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
