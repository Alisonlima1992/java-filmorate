package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	@Autowired
	private FilmController filmController;

	@Autowired
	private UserController userController;

	@Test
	void testFilmValidation_ValidFilm() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);

		assertDoesNotThrow(() -> filmController.validateFilm(film), "Валидный фильм не должен вызывать исключения");
	}

	@Test
	void testFilmValidation_EmptyNameThrowsException() {
		Film film = new Film();
		film.setName("");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Пустое имя должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_NullNameThrowsException() {
		Film film = new Film();
		film.setName(null);
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Имя null должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_LongDescriptionThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Very long description ".repeat(20));
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Слишком длинное описание должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_EarlyReleaseDateThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(1894, 1, 1));
		film.setDuration(120);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Дата релиза раньше 1895-12-28 должна вызывать ValidationException");
	}

	@Test
	void testFilmValidation_NegativeDurationThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(-120);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Отрицательная продолжительность должна вызывать ValidationException");
	}

	@Test
	void testFilmValidation_ZeroDurationThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(0);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(film), "Нулевая продолжительность должна вызывать ValidationException");
	}

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