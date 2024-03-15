package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Repository
@Primary
public class JdbcFeedStorage implements FeedStorage {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcFeedStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Event add(Long userId, Long entityId, String operation, String eventType) {
		Event event = new Event(userId, entityId, EventOperation.valueOf(operation), EventType.valueOf(eventType),
				Instant.now().toEpochMilli());

		String sql = "INSERT INTO feed (user_id, entity_id, operation, event_type, event_timestamp) " +
				"VALUES (:userId, :entityId, :operation, :eventType, :event_timestamp)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", event.getUserId());
		params.addValue("entityId", event.getEntityId());
		params.addValue("operation", event.getOperation().toString());
		params.addValue("eventType", event.getEventType().toString());
		params.addValue("event_timestamp", event.getTimestamp());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, params, keyHolder);

		Long generatedId = keyHolder.getKey().longValue();
		event.setEventId(generatedId);

		return event;
	}

	@Override
	public List<Event> getByUserId(Long userId) {
		String sql = "SELECT * FROM feed WHERE user_id = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);

		return namedParameterJdbcTemplate.query(sql, params, JdbcFeedStorage::mapRowToEvent);
	}

	@Override
	public void delete(Long entityId) {
		String sql = "DELETE FROM feed WHERE entity_id = :entityId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("entityId", entityId);

		namedParameterJdbcTemplate.update(sql, params);
	}

	private static Event mapRowToEvent(ResultSet row, int rowNum) throws SQLException {
		Long eventId = row.getLong("event_id");
		Long userId = row.getLong("user_id");
		Long entityId = row.getLong("entity_id");
		EventOperation operation = EventOperation.valueOf(row.getString("operation"));
		EventType eventType = EventType.valueOf(row.getString("event_type"));
		Long timestamp = row.getLong("event_timestamp");

		return new Event(eventId, userId, entityId, operation, eventType, timestamp);
	}
}
