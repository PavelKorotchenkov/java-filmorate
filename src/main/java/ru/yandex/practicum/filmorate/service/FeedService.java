package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
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
        return feedStorage.addEvent(userId, entityId, operation, eventType);
    }

    public List<Event> getEvents(Long userId) {
        User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе
        return feedStorage.getEvents(userId);
    }

    public void deleteEvent(Long entityId) {
        feedStorage.deleteEvent(entityId);
    }
}
