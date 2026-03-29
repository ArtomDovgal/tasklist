package dev.dov.tasklist.service;

import dev.dov.tasklist.domain.task.TaskImage;

public interface ImageService {

    String upload(TaskImage image);
}
