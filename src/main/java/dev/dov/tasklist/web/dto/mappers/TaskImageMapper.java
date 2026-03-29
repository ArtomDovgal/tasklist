package dev.dov.tasklist.web.dto.mappers;

import dev.dov.tasklist.domain.task.TaskImage;
import dev.dov.tasklist.web.dto.task.ImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, ImageDto> {
}
