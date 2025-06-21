package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.Service.FilmService;
import ru.yandex.practicum.filmorate.Service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	@Autowired
	private FilmService filmService;

	@Autowired
	private UserService userService;

	@Autowired
	private InMemoryFilmStorage inMemoryFilmStorage;
	@Autowired
	private InMemoryUserStorage inMemoryUserStorage;

	@BeforeEach
	void setUp() throws Exception {
		Field filmsField = InMemoryFilmStorage.class.getDeclaredField("films");
		filmsField.setAccessible(true);
		((java.util.Map<?, ?>) filmsField.get(inMemoryFilmStorage)).clear();
		Field nextFilmIdField = InMemoryFilmStorage.class.getDeclaredField("nextId");
		nextFilmIdField.setAccessible(true);
		nextFilmIdField.set(inMemoryFilmStorage, 1L);

		Field usersField = InMemoryUserStorage.class.getDeclaredField("users");
		usersField.setAccessible(true);
		((java.util.Map<?, ?>) usersField.get(inMemoryUserStorage)).clear();
		Field nextUserIdField = InMemoryUserStorage.class.getDeclaredField("nextId");
		nextUserIdField.setAccessible(true);
		nextUserIdField.set(inMemoryUserStorage, 1L);
	}

	@Test
	void testFilmValidation_ValidFilm() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertDoesNotThrow(() -> filmService.create(film), "Валидный фильм не должен вызывать исключения");
		assertNotNull(film.getId(), "Фильм должен получить ID");
	}

	@Test
	void testFilmValidation_EmptyNameThrowsException() {
		Film film = new Film();
		film.setName("");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> filmService.create(film), "Пустое имя должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_NullNameThrowsException() {
		Film film = new Film();
		film.setName(null);
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> filmService.create(film), "Имя null должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_LongDescriptionThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Very long description ".repeat(20));
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> filmService.create(film), "Слишком длинное описание должно вызывать ValidationException");
	}

	@Test
	void testFilmValidation_EarlyReleaseDateThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(1894, 1, 1));
		film.setDuration(120);
		assertThrows(ValidationException.class, () -> filmService.create(film), "Дата релиза раньше 1895-12-28 должна вызывать ValidationException");
	}

	@Test
	void testFilmValidation_NegativeDurationThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(-120);
		assertThrows(ValidationException.class, () -> filmService.create(film), "Отрицательная продолжительность должна вызывать ValidationException");
	}

	@Test
	void testFilmValidation_ZeroDurationThrowsException() {
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2023, 1, 1));
		film.setDuration(0);
		assertThrows(ValidationException.class, () -> filmService.create(film), "Нулевая продолжительность должна вызывать ValidationException");
	}

	@Test
	void testUserValidation_ValidUser() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertDoesNotThrow(() -> userService.create(user), "Валидный пользователь не должен вызывать исключения");
		assertNotNull(user.getId(), "Пользователь должен получить ID");
	}

	@Test
	void testUserValidation_EmptyEmailThrowsException() {
		User user = new User();
		user.setEmail("");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Пустой email должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_NullEmailThrowsException() {
		User user = new User();
		user.setEmail(null);
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Email null должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_EmailWithoutAtThrowsException() {
		User user = new User();
		user.setEmail("testtest.com");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Email без @ должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_EmptyLoginThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Пустой логин должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_NullLoginThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin(null);
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Логин null должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_LoginWithSpaceThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("test Login");
		user.setName("Test Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Логин с пробелом должен вызывать ValidationException");
	}

	@Test
	void testUserValidation_FutureBirthdayThrowsException() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("testLogin");
		user.setName("Test Name");
		user.setBirthday(LocalDate.now().plusDays(1));
		assertThrows(ValidationException.class, () -> userService.create(user), "Дата рождения в будущем должна вызывать ValidationException");
	}

	@Test
	void testUserValidation_NameIsEmptySetsLoginAsName() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("testLogin");
		user.setName("");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		userService.create(user);
		assertEquals("testLogin", user.getName(), "Если имя пустое, должно быть установлено значение логина");
	}

	@Test
	void testUserValidation_NameIsNullSetsLoginAsName() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLogin("testLogin");
		user.setName(null);
		user.setBirthday(LocalDate.of(1990, 1, 1));
		userService.create(user);
		assertEquals("testLogin", user.getName(), "Если имя null, должно быть установлено значение логина");
	}

	@Test
	void testAddFriend() {
		User user1 = new User();
		user1.setEmail("user1@mail.com"); user1.setLogin("user1"); user1.setName("User One"); user1.setBirthday(LocalDate.of(2000, 1, 1));
		user1 = userService.create(user1);

		User user2 = new User();
		user2.setEmail("user2@mail.com"); user2.setLogin("user2"); user2.setName("User Two"); user2.setBirthday(LocalDate.of(2001, 2, 2));
		user2 = userService.create(user2);

		userService.addFriend(user1.getId(), user2.getId());

		assertTrue(userService.getFriends(user1.getId()).contains(user2), "User1 should have User2 as friend");
		assertTrue(userService.getFriends(user2.getId()).contains(user1), "User2 should have User1 as friend (mutual)");
	}

	@Test
	void testDeleteFriend() {
		User user1 = new User();
		user1.setEmail("user1@mail.com"); user1.setLogin("user1"); user1.setName("User One"); user1.setBirthday(LocalDate.of(2000, 1, 1));
		user1 = userService.create(user1);

		User user2 = new User();
		user2.setEmail("user2@mail.com"); user2.setLogin("user2"); user2.setName("User Two"); user2.setBirthday(LocalDate.of(2001, 2, 2));
		user2 = userService.create(user2);

		userService.addFriend(user1.getId(), user2.getId());
		assertTrue(userService.getFriends(user1.getId()).contains(user2), "User1 should have User2 as friend before deletion");

		userService.deleteFriend(user1.getId(), user2.getId());
		assertFalse(userService.getFriends(user1.getId()).contains(user2), "User1 should not have User2 as friend after deletion");
		assertFalse(userService.getFriends(user2.getId()).contains(user1), "User2 should not have User1 as friend after deletion");
	}

	@Test
	void testGetCommonFriends() {
		User user1 = new User(); user1.setEmail("user1@mail.com"); user1.setLogin("user1"); user1.setName("User One"); user1.setBirthday(LocalDate.of(2000, 1, 1));
		user1 = userService.create(user1);

		User user2 = new User(); user2.setEmail("user2@mail.com"); user2.setLogin("user2"); user2.setName("User Two"); user2.setBirthday(LocalDate.of(2001, 2, 2));
		user2 = userService.create(user2);

		User user3 = new User(); user3.setEmail("user3@mail.com"); user3.setLogin("user3"); user3.setName("User Three"); user3.setBirthday(LocalDate.of(2002, 3, 3));
		user3 = userService.create(user3);

		userService.addFriend(user1.getId(), user2.getId());
		userService.addFriend(user1.getId(), user3.getId());
		userService.addFriend(user2.getId(), user3.getId());

		List<User> commonFriends = userService.getCommonFriends(user1.getId(), user2.getId());
		assertEquals(1, commonFriends.size(), "Should have 1 common friend");
		assertTrue(commonFriends.contains(user3), "Common friend should be User3");
	}

	@Test
	void testGetCommonFriends_NoCommonFriends() {
		User user1 = new User(); user1.setEmail("user1@mail.com"); user1.setLogin("user1"); user1.setName("User One"); user1.setBirthday(LocalDate.of(2000, 1, 1));
		user1 = userService.create(user1);

		User user2 = new User(); user2.setEmail("user2@mail.com"); user2.setLogin("user2"); user2.setName("User Two"); user2.setBirthday(LocalDate.of(2001, 2, 2));
		user2 = userService.create(user2);

		User user3 = new User(); user3.setEmail("user3@mail.com"); user3.setLogin("user3"); user3.setName("User Three"); user3.setBirthday(LocalDate.of(2002, 3, 3));
		user3 = userService.create(user3);

		userService.addFriend(user1.getId(), user2.getId());
		List<User> commonFriends = userService.getCommonFriends(user1.getId(), user3.getId());
		assertTrue(commonFriends.isEmpty(), "Should have no common friends");
	}

	@Test
	void testAddLike() {
		Film film = new Film();
		film.setName("Film Title"); film.setDescription("Desc"); film.setReleaseDate(LocalDate.of(2000, 1, 1)); film.setDuration(90);
		film = filmService.create(film);

		User user = new User();
		user.setEmail("user@mail.com"); user.setLogin("user"); user.setName("User Name"); user.setBirthday(LocalDate.of(1990, 1, 1));
		user = userService.create(user);

		filmService.addLike(film.getId(), user.getId());

		Film updatedFilm = filmService.getFilmById(film.getId());
		Set<Long> likes = updatedFilm.getLikes();

		assertEquals(1, likes.size(), "Film should have 1 like");
	}

	@Test
	void testAddLike_DuplicateLikeIgnored() {
		Film film = new Film();
		film.setName("Film Title"); film.setDescription("Desc"); film.setReleaseDate(LocalDate.of(2000, 1, 1)); film.setDuration(90);
		film = filmService.create(film);

		User user = new User();
		user.setEmail("user@mail.com"); user.setLogin("user"); user.setName("User Name"); user.setBirthday(LocalDate.of(1990, 1, 1));
		user = userService.create(user);

		filmService.addLike(film.getId(), user.getId());
		filmService.addLike(film.getId(), user.getId());

		Film updatedFilm = filmService.getFilmById(film.getId());
		assertEquals(1, updatedFilm.getLikes().size(), "Duplicate like should be ignored, film should still have 1 like");
	}

	@Test
	void testDeleteLike() {
		Film film = new Film();
		film.setName("Film Title"); film.setDescription("Desc"); film.setReleaseDate(LocalDate.of(2000, 1, 1)); film.setDuration(90);
		film = filmService.create(film);

		User user = new User();
		user.setEmail("user@mail.com"); user.setLogin("user"); user.setName("User Name"); user.setBirthday(LocalDate.of(1990, 1, 1));
		user = userService.create(user);

		filmService.addLike(film.getId(), user.getId());
		assertEquals(1, filmService.getFilmById(film.getId()).getLikes().size(), "Film should have 1 like before deletion");

		filmService.deleteLike(film.getId(), user.getId());
		assertTrue(filmService.getFilmById(film.getId()).getLikes().isEmpty(), "Film should have no likes after deletion");
	}

	@Test
	void testDeleteLike_UserDidNotLike() {
		Film film = new Film();
		film.setName("Film Title"); film.setDescription("Desc"); film.setReleaseDate(LocalDate.of(2000, 1, 1)); film.setDuration(90);
		film = filmService.create(film);

		User user = new User();
		user.setEmail("user@mail.com"); user.setLogin("user"); user.setName("User Name"); user.setBirthday(LocalDate.of(1990, 1, 1));
		user = userService.create(user);

		User user2 = new User();
		user2.setEmail("user2@mail.com"); user2.setLogin("user2"); user2.setName("User Two"); user2.setBirthday(LocalDate.of(1990, 1, 1));
		user2 = userService.create(user2);

		filmService.addLike(film.getId(), user.getId());
		filmService.deleteLike(film.getId(), user2.getId());

		assertEquals(1, filmService.getFilmById(film.getId()).getLikes().size(), "Like count should not change if user did not like");
	}

	@Test
	void testGetPopularFilms() {
		Film film1 = new Film(); film1.setName("Film 1"); film1.setDescription("Desc"); film1.setReleaseDate(LocalDate.of(2000, 1, 1)); film1.setDuration(90);
		film1 = filmService.create(film1);

		Film film2 = new Film(); film2.setName("Film 2"); film2.setDescription("Desc"); film2.setReleaseDate(LocalDate.of(2001, 1, 1)); film2.setDuration(100);
		film2 = filmService.create(film2);

		Film film3 = new Film(); film3.setName("Film 3"); film3.setDescription("Desc"); film3.setReleaseDate(LocalDate.of(2002, 1, 1)); film3.setDuration(110);
		film3 = filmService.create(film3);

		User user1 = new User(); user1.setEmail("u1@mail.com"); user1.setLogin("u1"); user1.setName("U1"); user1.setBirthday(LocalDate.of(1990, 1, 1));
		user1 = userService.create(user1);

		User user2 = new User(); user2.setEmail("u2@mail.com"); user2.setLogin("u2"); user2.setName("U2"); user2.setBirthday(LocalDate.of(1991, 1, 1));
		user2 = userService.create(user2);

		User user3 = new User(); user3.setEmail("u3@mail.com"); user3.setLogin("u3"); user3.setName("U3"); user3.setBirthday(LocalDate.of(1992, 1, 1));
		user3 = userService.create(user3);

		filmService.addLike(film1.getId(), user1.getId());
		filmService.addLike(film1.getId(), user2.getId());

		filmService.addLike(film2.getId(), user1.getId());

		List<Film> popularFilms = filmService.getPopularFilms(2);

		assertEquals(2, popularFilms.size());
		assertEquals(film1.getName(), popularFilms.get(0).getName(), "Film 1 should be most popular");
		assertEquals(film2.getName(), popularFilms.get(1).getName(), "Film 2 should be second most popular");

		List<Film> allPopularFilms = filmService.getPopularFilms(10);
		assertEquals(3, allPopularFilms.size());
		assertEquals(film1.getName(), allPopularFilms.get(0).getName());
		assertEquals(film2.getName(), allPopularFilms.get(1).getName());
		assertEquals(film3.getName(), allPopularFilms.get(2).getName());
	}

	@Test
	void testGetPopularFilms_DefaultCount() {
		Film film1 = new Film(); film1.setName("Film 1"); film1.setDescription("Desc"); film1.setReleaseDate(LocalDate.of(2000, 1, 1)); film1.setDuration(90);
		film1 = filmService.create(film1);
		Film film2 = new Film(); film2.setName("Film 2"); film2.setDescription("Desc"); film2.setReleaseDate(LocalDate.of(2001, 1, 1)); film2.setDuration(100);
		film2 = filmService.create(film2);
		Film film3 = new Film(); film3.setName("Film 3"); film3.setDescription("Desc"); film3.setReleaseDate(LocalDate.of(2002, 1, 1)); film3.setDuration(110);
		film3 = filmService.create(film3);

		List<Film> popularFilms = filmService.getPopularFilms(10);
		assertEquals(3, popularFilms.size());
	}
}