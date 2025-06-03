package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void testFilmValidation_ValidFilm() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertDoesNotThrow(() -> validateFilm(film), "Валидный фильм не должен вызывать исключения");
	}

	@Test
	void testFilmValidation_EmptyNameThrowsException() {
		Film film = new Film();
		film.setName("");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> validateFilm(film), "Пустое имя должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_NullNameThrowsException() {
		Film film = new Film();
		film.setName(null);
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> validateFilm(film), "Имя null должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_LongDescriptionThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Very long description ".repeat(20));
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> validateFilm(film), "Слишком длинное описание должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_EarlyReleaseDateThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(1894, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> validateFilm(film), "Дата релиза раньше 1895-12-28 должна вызывать ValidationException");
	}

	@Test
	void testFilmValidation_NegativeDurationThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(-120);
		assertThrows(ValidationException.class, () -> validateFilm(film), "Отрицательная продолжительность должна вызывать ValidationException");
	}

	@Test
	void testFilmValidation_ZeroDurationThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(0);
		assertThrows(ValidationException.class, () -> validateFilm(film), "Нулевая продолжительность должна вызывать ValidationException");
	}

	@Test
	void testUserValidation_ValidUser() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertDoesNotThrow(() -> validateUser(user), "Валидный пользователь не должен вызывать исключения");
	}

	@Test
	void testUserValidation_EmptyEmailThrowsException() {
		User user = new User();
		user.setEmail("");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Пустой email должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_NullEmailThrowsException() {
		User user = new User();
		user.setEmail(null);
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Email null должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_EmailWithoutAtThrowsException() {
		User user = new User();
		user.setEmail("testtest.com");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Email без @ должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_EmptyLoginThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Пустой логин должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_NullLoginThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin(null);
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Логин null должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_LoginWithSpaceThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("test Login");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Логин с пробелом должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_FutureBirthdayThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(2026, 1, 1));
		assertThrows(ValidationException.class, () -> validateUser(user), "Дата рождения в будущем должна вызывать ValidationException");
	}

	private void validateFilm(Film film) {
		if (film.getName() == null || film.getName().isEmpty()) {
			throw new ValidationException("Название фильма не может быть пустым.");
		}
		if (film.getDescription() != null && film.getDescription().length() > 200) {
			throw new ValidationException("Максимальная длина описания фильма — 200 символов.");
		}
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
			throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
		}
		if (film.getDuration() != null && film.getDuration() <= 0) {
			throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
		}
	}

	private void validateUser(User user) {
		if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
			throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
		}
		if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
			throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
		}
		if (user.getName() == null || user.getName().isEmpty()) {
			user.setName(user.getLogin());
		}
		if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("Дата рождения не может быть в будущем.");
		}
	}
}