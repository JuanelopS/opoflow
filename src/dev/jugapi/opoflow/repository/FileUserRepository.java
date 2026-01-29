package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {

    private final String usersFilename = "users.txt";
    private static final int INDEX_USER_UUID = 0;
    private static final int INDEX_USER_NAME = 1;

    @Override
    public void save(User user) {
        try {
            String line = user.getId().toString() + ";" + user.getName() + System.lineSeparator();
            Files.writeString(
                    Path.of(usersFilename),
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (
                IOException e) {
            throw new RuntimeException("Error al guardar el usuario", e);
        }
    }

    @Override
    public User findByName(String name) {
        Path path = Path.of(usersFilename);
        if (!Files.exists(path)) {
            return null;
        }
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(line -> {
                        String[] parts = line.split(";");
                        return new User(UUID.fromString(parts[INDEX_USER_UUID]), parts[INDEX_USER_NAME]);
                    })
                    .filter(user -> user.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(UUID id) {
        Path path = Path.of(usersFilename);
        if (!Files.exists(path)) {
            return null;
        }
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(line -> {
                        String[] parts = line.split(";");
                        return new User(UUID.fromString(parts[INDEX_USER_UUID]), parts[INDEX_USER_NAME]);
                    })
                    .filter(user -> user.getId().toString().equalsIgnoreCase(id.toString()))
                    .findFirst()
                    .orElse(null);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }


}