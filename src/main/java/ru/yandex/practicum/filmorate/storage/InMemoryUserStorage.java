package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public User create(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден для обновления.", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь успешно обновлен: {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.info("Получен запрос на получение списка всех пользователей.");
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long id) {
        log.info("Получен запрос на получение пользователя с id: {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void delete(long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id {} не найден для удаления.", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
        users.remove(id);
        log.info("Пользователь с id {} успешно удален.", id);
    }
}