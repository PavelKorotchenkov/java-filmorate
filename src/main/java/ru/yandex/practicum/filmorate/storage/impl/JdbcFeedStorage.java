package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@Primary
public class JdbcFeedStorage implements FeedStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcFeedStorage(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Event> add(Long userId, Long entityId, String operation, String eventType) {
        Event event = new Event(userId, entityId, EventOperation.valueOf(operation), EventType.valueOf(eventType), Instant.now().toEpochMilli());

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO feed (user_id, entity_id, operation, event_type, event_timestamp) " +
                "VALUES (:user_id, :entity_id, :operation, :event_type, :event_timestamp)";

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource(toMap(event)),
                generatedKeyHolder);

        event.setEventId(generatedKeyHolder.getKey().longValue());

        return Optional.of(event);
    }

    @Override
    public List<Event> getAll(Long userId) {
        return namedParameterJdbcTemplate.query(
                "SELECT * " +
                        "FROM feed " +
                        "WHERE user_id = :user_id",
                new MapSqlParameterSource("user_id", userId),
                RowMapper::mapRowToEvent);
    }

    @Override
    public void delete(Long entityId) {
        String sqlQuery = "DELETE FROM feed WHERE entity_id = :entity_id";
        namedParameterJdbcTemplate.update(
                sqlQuery,
                new MapSqlParameterSource("entity_id",
                        entityId));
    }

    public Map<String, Object> toMap(Event event) {
        Map<String, Object> values = new HashMap<>();
        values.put("event_id", event.getEventId());
        values.put("user_id", event.getUserId());
        values.put("entity_id", event.getEntityId());
        values.put("operation", event.getOperation().toString());
        values.put("event_type", event.getEventType().toString());
        values.put("event_timestamp", event.getTimestamp());
        return values;
    }
}
