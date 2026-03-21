package dev.dov.tasklist.web.dto.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.domain.user.Role;
import dev.dov.tasklist.web.dto.validation.OnCreate;
import dev.dov.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data
@Schema(description = "User DTO")
public class UserDto {

    @Schema(description = "user id", example = "1")
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "user name", example = "John Doe")
    @NotNull(message = "Name must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols" , groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @Schema(description = "user email", example = "johndoe@gmail.com")
    @NotNull(message = "Username must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols" , groups = {OnUpdate.class, OnCreate.class})
    private String username;

    @Schema(description = "user encrypted password", example = "$2a$12$Uq39kDgtJrb8W920qdWcDed4M7mYyuVR4/OsV9JUvZAjOtHk0wuZ.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(description = "User password confirmation", example = "$2a$12$Uq39kDgtJrb8W920qdWcDed4M7mYyuVR4/OsV9JUvZAjOtHk0wuZ.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = OnCreate.class)
    private String passwordConfirmation;

}
