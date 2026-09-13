package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.config.TestConfig;
import dev.dov.tasklist.domain.exeption.ResourceNotFoundException;
import dev.dov.tasklist.domain.user.Role;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.repository.TaskRepository;
import dev.dov.tasklist.repository.UserRepository;
import dev.dov.tasklist.service.UserService;
import dev.dov.tasklist.web.dto.auth.JwtRequest;
import dev.dov.tasklist.web.dto.auth.JwtResponse;
import dev.dov.tasklist.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
public class AuthServiceImplTest {

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthServiceImpl authService;


    @Test
    void login() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);
        Mockito.when(userService.getByUsername(username)).thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, username, roles)).thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username)).thenReturn(refreshToken);
        //when
        JwtResponse response = authService.login(request);
        //then
        assertEquals(username, response.getUsername());
        assertEquals(userId, response.getId());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

    }

    @Test
    void loginWithIncorrectUsername() {

        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        Mockito.when(userService.getByUsername(username)).thenThrow(ResourceNotFoundException.class);

        Mockito.verifyNoInteractions(tokenProvider);
        assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void refresh() {
        //given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserTokens(refreshToken)).thenReturn(response);
        //when
        JwtResponse testResponse = authService.refresh(refreshToken);
        //then
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        assertEquals(response, testResponse);

    }
}
