package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {

    private final String usersFilename = "users.txt";


    @Override
    public void save(User user) {

    }

    @Override
    public User findByName(String name) {
        return null;
    }

}
