package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserStorage userStorage;

    public FeedService(FeedStorage feedStorage, UserStorage userStorage) {
        this.feedStorage = feedStorage;
        this.userStorage = userStorage;
    }

    public Optional<Event> add(Long userId, Long entityId, String operation, String eventType) {
        return feedStorage.add(userId, entityId, operation, eventType);
    }

    public List<Event> getAll(Long userId) {
        User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе
        return feedStorage.getAll(userId);
    }

    public void delete(Long entityId) {
        feedStorage.delete(entityId);
    }
}
