package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.UserRepository;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User loginOrRegister(String name) {
        User user = this.repository.findByName(name);
        if (user == null) {
            User u = new User(name);
            repository.save(u);
            return u;
        }
        return user;
    }
}
