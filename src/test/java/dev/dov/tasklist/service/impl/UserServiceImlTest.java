package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.config.TestConfig;
import dev.dov.tasklist.domain.exeption.ResourceNotFoundException;
import dev.dov.tasklist.domain.user.Role;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.repository.TaskRepository;
import dev.dov.tasklist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
public class UserServiceImlTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private AuthenticationManager manager;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        //given
        Long id = 1L;
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        //when
        User testUser = userService.getById(id);
        //then
        assertEquals(user, testUser);
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByNotExistsId() {
        //given
        Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        //given
        String username = "simple username";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        //when
        User testUser = userService.getByUsername(username);
        //then
        assertEquals(user, testUser);
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void getByNotExistsUsername() {
        //given
        String username = "simple username";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void update() {
        //given
        String password = "password";
        User user = new User();
        user.setPassword(password);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("encoded password");
        //when
        userService.update(user);
        //then
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void isTaskOwner(){
        //given
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId,taskId)).thenReturn(true);
        //when
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        //then
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        assertTrue(isOwner);
    }

    @Test
    void create() {
        //given
        String username = "username";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        //when
        User testUser = userService.create(user);
        //then
        Mockito.verify(userRepository).save(user);
        Mockito.verify(passwordEncoder).encode(password);
        assertEquals(Set.of(Role.ROLE_USER), user.getRoles());
    }

    @Test
    void createWithExistingUsername() {
        //given
        String username = "username";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        //when
        //then
        assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPassword() {
        //given
        String username = "username";
        String password = "password";
        String passwordConfirmation = "passwordConfirmation";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }

}
