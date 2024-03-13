package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Primary
public class JdbcFeedStorage implements FeedStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcFeedStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Event add(Long userId, Long entityId, String operation, String eventType) {
		Event event = new Event(userId, entityId, EventOperation.valueOf(operation), EventType.valueOf(eventType), Instant.now().toEpochMilli());
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("feed")
				.usingGeneratedKeyColumns("event_id");
		event.setEventId(simpleJdbcInsert.executeAndReturnKey(toMap(event)).longValue());
		return event;
	}

	@Override
	public List<Event> getByUserId(Long userId) {
		return jdbcTemplate.query(
				"SELECT * " +
						"FROM feed " +
						"WHERE user_id = ?",
				RowMapper::mapRowToEvent,
				userId);
	}

	@Override
	public void delete(Long entityId) {
		String sqlQuery = "DELETE FROM feed WHERE entity_id = ?";
		int recordsAffected = jdbcTemplate.update(sqlQuery, entityId);
		if (recordsAffected == 0) {
			throw new NotFoundException("Сущность события с id " + entityId + " не найдена");
		}
	}

	private Map<String, Object> toMap(Event event) {
		Map<String, Object> values = new HashMap<>();
		values.put("event_id", event.getEventId());
		values.put("user_id", event.getUserId());
		values.put("entity_id", event.getEntityId());
		values.put("operation", event.getOperation());
		values.put("event_type", event.getEventType());
		values.put("event_timestamp", event.getTimestamp());
		return values;
	}
}
