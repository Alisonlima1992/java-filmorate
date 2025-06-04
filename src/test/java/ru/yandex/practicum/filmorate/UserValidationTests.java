package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserValidationTests {
    @Autowired
    private UserController userController;
    @Test
    void testUserValidation_ValidUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertDoesNotThrow(() -> userController.validateUser(user), "Валидный пользователь не должен вызывать исключения");
    }

    @Test
    void testUserValidation_EmptyEmailThrowsException() {
        User user = new User();
        user.setEmail("");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Пустой email должен вызывать ValidationException");
    }

    @Test
    void testUserValidation_NullEmailThrowsException() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Email null должен вызывать ValidationException");
    }

    @Test
    void testUserValidation_EmailWithoutAtThrowsException() {
        User user = new User();
        user.setEmail("testtest.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Email без @ должен вызывать ValidationException");
    }

    @Test
    void testUserValidation_EmptyLoginThrowsException() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Пустой логин должен вызывать ValidationException");
    }

    @Test
    void testUserValidation_NullLoginThrowsException() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin(null);
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Логин null должен вызывать ValidationException");
    }

    @Test
    void testUserValidation_LoginWithSpaceThrowsException() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test Login");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Логин с пробелом должен вызывать ValidationException");
    }

    @Test
    void testUserValidation_FutureBirthdayThrowsException() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.validateUser(user), "Дата рождения в будущем должна вызывать ValidationException");
    }
}