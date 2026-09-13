package dev.dov.tasklist.repository;

import dev.dov.tasklist.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t
            JOIN user_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("user_id") Long userId);


    @Query(value = """
            SElECT * FROM tasks t
            WHERE t.expiration_date is not null
            AND t.expiration_date between :start and :end
            """, nativeQuery = true)
    List<Task> findAllSoonTasks(@Param("start")Timestamp start, @Param("end") Timestamp end);

    //@Modifying
}
