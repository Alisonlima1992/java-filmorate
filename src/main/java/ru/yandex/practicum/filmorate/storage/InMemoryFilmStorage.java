package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film create(Film film) {
        film.setId((int) nextId++);
        films.put(film.getId().longValue(), film);
        log.info("Фильм успешно добавлен: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId().longValue())) {
            log.warn("Фильм с id {} не найден для обновления.", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден.");
        }
        films.put(film.getId().longValue(), film);
        log.info("Фильм успешно обновлен: {}", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        log.info("Получен запрос на получение списка всех фильмов.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getById(long id) {
        log.info("Получен запрос на получение фильма с id: {}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void delete(long id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм с id {} не найден для удаления.", id);
            throw new NotFoundException("Фильм с id " + id + " не найден.");
        }
        films.remove(id);
        log.info("Фильм с id {} успешно удален.", id);
    }
}