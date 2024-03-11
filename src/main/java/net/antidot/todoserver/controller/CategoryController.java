package net.antidot.todoserver.controller;

import net.antidot.todoserver.model.Category;
import net.antidot.todoserver.model.WsCategoryCreation;
import net.antidot.todoserver.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {
    private final CategoryRepository repository;

    @Autowired
    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/categories")
    public List<Category> getCategoryList() {
        return repository.listAll();
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody WsCategoryCreation creation) throws URISyntaxException {

        Category category = new Category(
                UUID.randomUUID().toString(),
                creation.name(),
                creation.color()
        );
        repository.save(category);

        return ResponseEntity.created(new URI("/categories/" + category.id()))
                .body(category);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable String id) {
        repository.delete(id);
    }


}
