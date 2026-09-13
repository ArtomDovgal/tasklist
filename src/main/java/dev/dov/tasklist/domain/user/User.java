package dev.dov.tasklist.domain.user;

import dev.dov.tasklist.domain.task.Task;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String password;
    @Transient
    private String passwordConfirmation;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name  = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

//    @CollectionTable(name = "users_tasks")
//    @JoinColumn(name = "task_id")
@OneToMany
@JoinTable(
        name = "users_tasks",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id")
)
    private List<Task> tasks;
}
