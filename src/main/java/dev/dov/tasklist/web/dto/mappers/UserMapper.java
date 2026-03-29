package dev.dov.tasklist.web.dto.mappers;

import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

}
