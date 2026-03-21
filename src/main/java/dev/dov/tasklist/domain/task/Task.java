package dev.dov.tasklist.domain.task;

import jakarta.persistence.Entity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

//@Entity
@Data
public class Task implements Serializable{

    private Long id;
    private String title;
    private String  description;
    private Status status;
    private LocalDateTime expirationDate;
}
