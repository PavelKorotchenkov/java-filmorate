package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Slf4j
@Repository
@Primary
public class FeedDbStorage implements FeedStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FeedDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Event addEvent(Long userId, Long entityId, String operation, String eventType) {
        Event event = new Event(userId, entityId, EventOperation.valueOf(operation), EventType.valueOf(eventType), Instant.now().toEpochMilli());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("feed")
                .usingGeneratedKeyColumns("event_id");
        event.setEventId(simpleJdbcInsert.executeAndReturnKey(event.toMap()).longValue());
        log.info("Создан event: {}", event);
        return event;
    }

    @Override
    public List<Event> getEvents(Long userId) {
        log.info("Запрос ленты по id: {}", userId);

        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM feed " +
                        "WHERE user_id = ?",
                RowMapper::mapRowToEvent,
                userId);
    }

    @Override
    public void deleteEvent(Long entityId) {
        String sqlQuery = "DELETE FROM feed WHERE entity_id = ?";
        int recordsAffected = jdbcTemplate.update(sqlQuery, entityId);
        if (recordsAffected == 0) {
            log.warn("Удаление события - Событие с id сущности {} не найдено", entityId);
            throw new NotFoundException("Сущность события с id " + entityId + " не найдена");
        } else {
            log.info("Событие с сущностью id {} удалено", entityId);
        }
    }
}
