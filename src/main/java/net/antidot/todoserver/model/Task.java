package net.antidot.todoserver.model;

import java.util.Optional;

public record Task(String id, String name, TaskStatus status, Optional<String> categoryId) {
}
