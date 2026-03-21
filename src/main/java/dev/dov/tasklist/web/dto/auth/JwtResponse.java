package dev.dov.tasklist.web.dto.auth;


import dev.dov.tasklist.web.dto.validation.OnCreate;
import dev.dov.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class JwtResponse {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Name must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols" , groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @NotNull(message = "Username must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols" , groups = {OnUpdate.class, OnCreate.class})
    private String username;

    private String accessToken;

    private String refreshToken;

}
