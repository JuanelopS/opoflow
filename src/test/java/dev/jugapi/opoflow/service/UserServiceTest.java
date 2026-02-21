package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("Should return existing user (no create new user)")
    void testLoginOrRegister_userExists() {
        String name = "test";
        User existingUser = new User(name);

        Mockito.when(repository.findByName("test")).thenReturn(existingUser);

        User result = service.loginOrRegister(name);

        assertEquals(existingUser, result);

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should save new user (user no exists)")
    void testLoginOrRegister_userNoExists() {
        String name = "new_user";

        Mockito.when(repository.findByName(name)).thenReturn(null);

        User result = service.loginOrRegister(name);

        assertEquals(name, result.getName());

        Mockito.verify(repository).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should return an user")
    void testFindById() {
        UUID id = UUID.randomUUID();
        User user = new User(id);

        Mockito.when(repository.findById(id)).thenReturn(user);

        User result = service.findById(id);

        assertEquals(user, result);

        Mockito.verify(repository).findById(id);
    }
}
