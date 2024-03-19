package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface FeedStorage {
	Event add(Long userId, Long entityId, String operation, String eventType);

	List<Event> getByUserId(Long userId);

	void delete(Long entityId);
}
