package dev.jugapi.opoflow.model.user;

import java.util.UUID;

public class User {
    private final UUID id;
    private String name;

    public User(UUID id) {
        this.id = id;
    }

    public User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
