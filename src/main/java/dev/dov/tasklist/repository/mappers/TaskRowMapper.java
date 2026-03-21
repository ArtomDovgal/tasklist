package dev.dov.tasklist.repository.mappers;

import dev.dov.tasklist.domain.task.Task;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskRowMapper {

    @SneakyThrows
    public static Task mapRow(ResultSet resultSet){

        if(resultSet.next()){

            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            task.setTitle(resultSet.getString("task_title"));
            task.setDescription(resultSet.getString("task_description"));
            if(resultSet.getTimestamp("task_expiration_date") != null){
                task.setExpirationDate(resultSet.getTimestamp("task_expiration_date").toLocalDateTime());
            }

            return task;
        }

        return null;
    }

    @SneakyThrows
    public static List<Task> mapRows(ResultSet resultSet){

        List<Task> tasks = new ArrayList<>();
        while(resultSet.next()){
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));

            if(!resultSet.wasNull()){
                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));
                if (resultSet.getTimestamp("task_expiration_date") != null) {
                    task.setExpirationDate(resultSet.getTimestamp("task_expiration_date").toLocalDateTime());
                }
                tasks.add(task);
            }
        }

        return tasks;
    }
}
