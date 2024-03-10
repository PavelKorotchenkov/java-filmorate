package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedStorage {
    Event addEvent(Long userId, Long entityId, String operation, String eventType);

    List<Event> getEvents(Long userId);

    void deleteEvent(Long entityId);
}
