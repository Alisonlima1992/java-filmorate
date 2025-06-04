package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	@Autowired
	private FilmController filmController;

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
		Film filmWithEmptyName = new Film();
		filmWithEmptyName.setName("");
		filmWithEmptyName.setDescription("Test Description");
		filmWithEmptyName.setReleaseDate(LocalDate.of(2023, 1, 1));
		filmWithEmptyName.setDuration(120);

		assertThrows(ValidationException.class, () -> filmController.validateFilm(filmWithEmptyName), "Пустое имя должно вызывать ValidationException");
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
}