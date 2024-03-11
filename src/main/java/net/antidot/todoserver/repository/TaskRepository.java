package net.antidot.todoserver.repository;

import net.antidot.todoserver.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class TaskRepository {
    private List<Task> tasks = List.of();

    public List<Task> listAll() {
        return tasks;
    }

    public void save(Task task) throws TaskIdAlreadyExistException {
        if (tasks.stream().anyMatch(t -> t.id().equals(task.id()))) {
            throw new TaskIdAlreadyExistException();
        }
        tasks = Stream.concat(tasks.stream(), Stream.of(task)).toList();
    }

    public void delete(String id) {
        tasks = tasks.stream()
                .filter(task -> !task.id().equals(id))
                .toList();
    }

    public void update(Task task) {
        tasks = tasks.stream().map(t -> t.id().equals(task.id()) ? task : t).toList();
    }

    public boolean taskExist(String id) {
        return tasks.stream().anyMatch(task -> task.id().equals(id));
    }
}

