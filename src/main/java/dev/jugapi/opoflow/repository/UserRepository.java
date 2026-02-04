package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.user.User;

import java.util.UUID;

// TODO: Optional...
public interface UserRepository {
    void save(User user);
    User findByName(String name);
    User findById(UUID id);
}
