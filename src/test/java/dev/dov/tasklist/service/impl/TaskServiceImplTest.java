package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.config.TestConfig;
import dev.dov.tasklist.domain.exeption.ResourceNotFoundException;
import dev.dov.tasklist.domain.task.Status;
import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.domain.task.TaskImage;
import dev.dov.tasklist.repository.TaskRepository;
import dev.dov.tasklist.repository.UserRepository;
import dev.dov.tasklist.service.ImageService;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
public class TaskServiceImplTest {

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ImageService imageService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        //given
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
        //when
        Task testTask = taskService.getById(id);

        //then
        Mockito.verify(taskRepository).findById(id);
        assertEquals(task, testTask);
    }

    @Test
    void getByIdWhenNotExistingId() {
        //given
        Long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
        //when
        assertThrows(ResourceNotFoundException.class,
                ()-> taskService.getById(id));
        //then
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllUserById() {
        //given
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        //when
        List<Task> testTasks = taskService.getAllByUserId(userId);
        //then
        assertEquals(tasks, testTasks);
        Mockito.verify(taskRepository).findAllByUserId(userId);
    }

    @Test
    void update() {
        //given
        Task task = new Task();
        task.setStatus(Status.DONE);
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("description");
        task.setExpirationDate(LocalDateTime.now());
        //when
        Task testTask = taskService.update(task);
        //then
        Mockito.verify(taskRepository).save(task);
        assertEquals(task.getStatus(), testTask.getStatus());
        assertEquals(task.getTitle(), testTask.getTitle());
        assertEquals(task.getExpirationDate(), testTask.getExpirationDate());
    }

    @Test
    void updateWithNullStatus() {
        //given
        Task task = new Task();
        task.setId(1L);
        task.setTitle("title");
        task.setDescription("description");
        task.setExpirationDate(LocalDateTime.now());
        //when
        Task testTask = taskService.update(task);
        //then
        assertEquals(Status.TODO, testTask.getStatus());
        Mockito.verify(taskRepository).save(task);
    }

    /*
    @Test
    void create() {
        //given
        Long taskId = 1L;
        Long userId = 1L;
        Task task = new Task();
        Mockito.doAnswer(invocationOnMock -> {
           Task savedTask = invocationOnMock.getArgument(0);
            savedTask.setId(taskId);
            return savedTask;
        })
                .when(taskRepository).save(task);

        //when
        Task testTask = taskService.create(task, userId);

        //then
        assertNotNull(testTask.getId());
        Mockito.verify(taskRepository).save(task);
        Mockito.verify(taskRepository).ass
    }*/

    @Test
    void delete() {
        //given
        Long id = 1L;
        //when
        taskService.delete(id);
        //then
        Mockito.verify(taskRepository).deleteById(id);
    }
/*
    @Test
    void uploadImage() {
        Long id = 1L;

        String imageName = "imageName";
        TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.upload(taskImage))
                .thenReturn(imageName);
        //when
        taskService.uploadImage(id, taskImage);
        //then
        Mockito.verify(taskRepository).save()
    }
*/
}
