package dev.dov.tasklist.domain.user;

import dev.dov.tasklist.domain.task.Task;
import jakarta.persistence.Entity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

//@Entity
@Data
public class User implements Serializable{

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private List<Task> tasks;
}
