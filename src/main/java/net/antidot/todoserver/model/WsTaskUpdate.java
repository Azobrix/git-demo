package net.antidot.todoserver.model;

import java.util.Optional;

public record WsTaskUpdate(String name, TaskStatus status, Optional<String> categoryId) {
}
