package net.antidot.todoserver.repository;

import net.antidot.todoserver.model.Task;
import net.antidot.todoserver.model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class TaskRepositoryTest {
    TaskRepository repository = new TaskRepository();

    @Test
    void shouldCreateAndListTask() throws TaskIdAlreadyExistException {

        repository.save(new Task("task Id", "Task description", TaskStatus.TODO, Optional.empty()));
        var result = repository.listAll();

        Assertions.assertEquals(List.of(new Task("task Id", "Task description", TaskStatus.TODO, Optional.empty())), result);
    }

    @Test
    void shouldThrowWhenCreateTaskWithExistingId() throws TaskIdAlreadyExistException {

        repository.save(new Task("same Id", "Task description", TaskStatus.TODO, Optional.empty()));

        Assertions.assertThrows(TaskIdAlreadyExistException.class, () -> repository.save(new Task("same Id", "Other task description", TaskStatus.TODO, Optional.empty())));
    }

    @Test
    void shouldDeleteTask() throws TaskIdAlreadyExistException {
        repository.save(new Task("task Id", "Task description", TaskStatus.TODO, Optional.empty()));
        var result = repository.listAll();
        Assertions.assertEquals(1, result.size());

        repository.delete("task Id");
        result = repository.listAll();

        Assertions.assertEquals(0, result.size());
    }

    @Test
    void shouldUpdateStatus() throws TaskIdAlreadyExistException {
        repository.save(new Task("task Id", "Task description", TaskStatus.TODO, Optional.empty()));

        repository.update(new Task("task Id", "new description", TaskStatus.DONE, Optional.of("red")));
        var result = repository.listAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(new Task("task Id", "new description", TaskStatus.DONE, Optional.of("red")), result.get(0));
    }


}
