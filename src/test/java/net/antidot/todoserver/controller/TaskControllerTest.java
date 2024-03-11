package net.antidot.todoserver.controller;

import net.antidot.todoserver.model.Task;
import net.antidot.todoserver.model.TaskStatus;
import net.antidot.todoserver.model.WsTaskCreation;
import net.antidot.todoserver.model.WsTaskUpdate;
import net.antidot.todoserver.repository.CategoryRepository;
import net.antidot.todoserver.repository.TaskIdAlreadyExistException;
import net.antidot.todoserver.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    CategoryRepository categoryRepository = mock(CategoryRepository.class);
    TaskRepository repository = mock(TaskRepository.class);
    TaskController controller = new TaskController(categoryRepository, repository);

    @Test
    void shouldCreateTask() throws TaskIdAlreadyExistException, URISyntaxException {

        ResponseEntity<Task> response = controller.createTask(new WsTaskCreation("My todo", Optional.empty()));

        verify(repository).save(argThat(todo -> todo.name().equals("My todo") && todo.status().equals(TaskStatus.TODO)));
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldDelete() {

        controller.deleteTask("todo ID");

        verify(repository).delete("todo ID");
    }

    @Test
    void shouldUpdateTask() {
        when(repository.taskExist(any())).thenReturn(true);

        controller.updateTask(
                "todo ID",
                new WsTaskUpdate(
                        "new name",
                        TaskStatus.DONE,
                        Optional.of("new category Id"))
        );

        verify(repository).taskExist("todo ID");
        verify(repository).update(new Task("todo ID", "new name", TaskStatus.DONE, Optional.of("new category Id")));
    }

    @Test
    void shouldFailUpdateWhenTaskNotFound() {
        when(repository.taskExist(any())).thenReturn(false);

        Assertions.assertThrows(ResponseStatusException.class,
                () -> controller.updateTask("todo ID",
                        new WsTaskUpdate(
                                "new name",
                                TaskStatus.DONE,
                                Optional.of("new category Id"))
                )
        );

        verify(repository).taskExist("todo ID");
        verifyNoMoreInteractions(repository);
    }
}
