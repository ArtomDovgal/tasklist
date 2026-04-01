package dev.dov.tasklist.web.controller;

import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.service.AuthService;
import dev.dov.tasklist.service.UserService;
import dev.dov.tasklist.web.dto.auth.JwtRequest;
import dev.dov.tasklist.web.dto.auth.JwtResponse;
import dev.dov.tasklist.web.dto.auth.RefreshRequest;
import dev.dov.tasklist.web.dto.mappers.UserMapper;
import dev.dov.tasklist.web.dto.user.UserDto;
import dev.dov.tasklist.web.dto.validation.OnCreate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest jwtRequest){

        return authService.login(jwtRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto){

        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);

        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshRequest refreshRequest){

        return authService.refresh(refreshRequest.getRefreshToken());
    }
}
