package net.antidot.todoserver.repository;

import net.antidot.todoserver.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class CategoryRepository {
    private List<Category> categories = List.of();

    public List<Category> listAll() {
        return categories;
    }

    public void save(Category category) {
        categories = Stream.concat(categories.stream(), Stream.of(category)).toList();
    }

    public void delete(String id) {
        categories = categories.stream()
                .filter(c -> !c.id().equals(id))
                .toList();
    }

    public boolean categoryExist(String id) {
        return categories.stream().anyMatch(c -> c.id().equals(id));
    }
}
