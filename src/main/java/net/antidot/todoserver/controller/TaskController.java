package net.antidot.todoserver.controller;

import net.antidot.todoserver.model.Task;
import net.antidot.todoserver.model.TaskStatus;
import net.antidot.todoserver.model.WsTaskCreation;
import net.antidot.todoserver.model.WsTaskUpdate;
import net.antidot.todoserver.repository.CategoryRepository;
import net.antidot.todoserver.repository.TaskIdAlreadyExistException;
import net.antidot.todoserver.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
public class TaskController {
    private final CategoryRepository categoryRepository;
    private final TaskRepository repository;

    @Autowired
    public TaskController(CategoryRepository categoryRepository,
                          TaskRepository repository) {
        this.categoryRepository = categoryRepository;
        this.repository = repository;
    }

    @GetMapping("/tasks")
    public List<Task> getTaskList() {
        return repository.listAll();
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody WsTaskCreation creation) throws TaskIdAlreadyExistException, URISyntaxException {
        creation.categoryId().ifPresent(categoryId -> {
            if (!categoryRepository.categoryExist(categoryId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
            }
        });
        var task = new Task(
                UUID.randomUUID().toString(),
                creation.name(),
                TaskStatus.TODO,
                creation.categoryId()
        );

        repository.save(task);

        return ResponseEntity.created(new URI("/tasks/" + task.id()))
                .body(task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable String id) {
        repository.delete(id);
    }

    @PutMapping("/tasks/{id}")
    public void updateTask(@PathVariable String id, @RequestBody WsTaskUpdate update) {
        if (!repository.taskExist(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task does not exist");
        }
        repository.update(new Task(
                id,
                update.name(),
                update.status(),
                update.categoryId()
        ));
    }


}
