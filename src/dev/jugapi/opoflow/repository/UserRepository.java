package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.user.User;

public interface UserRepository {
    void save(User user);
    User findByName(String name);
}
