package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserStorage userStorage;

    public FeedService(FeedStorage feedStorage, UserStorage userStorage) {
        this.feedStorage = feedStorage;
        this.userStorage = userStorage;
    }

    public Event addEvent(Long userId, Long entityId, String operation, String eventType) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден.");
        }
        return feedStorage.add(userId, entityId, operation, eventType);
    }

    public List<Event> getEvents(Long userId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден.");
        }
        return feedStorage.getByUserId(user.getId());
    }

    public void deleteEvent(Long entityId) {
        feedStorage.delete(entityId);
    }
}
