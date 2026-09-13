package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.domain.MailType;
import dev.dov.tasklist.domain.task.Task;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.service.MailService;
import dev.dov.tasklist.service.Reminder;
import dev.dov.tasklist.service.TaskService;
import dev.dov.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder{

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration DURATION  = Duration.ofHours(1);


    @Scheduled(cron = "0 0 * * * * ")
    @Override
    public void remindForTask() {

        List<Task> taskList = taskService.getSoonTasks(DURATION);
        taskList.forEach( task -> {
            User user = userService.getById(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });

    }
}
