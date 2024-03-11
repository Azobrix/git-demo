package net.antidot.todoserver.model;

import java.util.Optional;

public record WsTaskCreation(String name, Optional<String> categoryId) {
}
