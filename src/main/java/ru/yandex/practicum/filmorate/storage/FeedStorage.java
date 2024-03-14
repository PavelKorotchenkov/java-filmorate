package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;
import java.util.Optional;

public interface FeedStorage {
    Optional<Event> add(Long userId, Long entityId, String operation, String eventType);

    List<Event> getAll(Long userId);

    void delete(Long entityId);
}
