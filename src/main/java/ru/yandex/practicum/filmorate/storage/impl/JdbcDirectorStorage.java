package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.util.List;

@Slf4j
@Repository
@Primary
public class JdbcDirectorStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcDirectorStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getAll() {
        log.info("Запрос всех режиссёров");
        return jdbcTemplate.query(
                "SELECT * " +
                        "FROM director " +
                        "ORDER by id",
                RowMapper::mapRowToDirector);
    }

    @Override
    public Director getById(Long directorId) {
        log.info("Запрос режиссёра по id");
        List<Director> directors = jdbcTemplate.query(
                "SELECT * " +
                        "FROM director " +
                        "WHERE id = ? ",
                RowMapper::mapRowToDirector,
                directorId);

        if (directors.size() != 1) {
            throw new NotFoundException("Режиссёр с идентификатором + " + directorId + " не найден.");
        }

        return directors.get(0);
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("id");
        director.setId(simpleJdbcInsert.executeAndReturnKey(director.toMap()).longValue());
        log.info("Создан режиссёр: {}", director);
        return director;
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "UPDATE director SET " +
                "name = ? " +
                "WHERE id = ?";
        log.warn("Попытка обновления режиссёра {}", director);
        int recordsAffected = jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId()
        );
        if (recordsAffected == 0) {
            log.warn("Обновление режиссёра - Режиссёр с id {} не найден", director.getId());
            throw new NotFoundException("Режиссёр с id " + director.getId() + " не найден");
        }
        return director;
    }

    @Override
    public void delete(Long directorId) {
        String sqlQuery = "DELETE FROM director WHERE id = ?";
        int recordsAffected = jdbcTemplate.update(sqlQuery, directorId);
        if (recordsAffected == 0) {
            log.warn("Удаление режиссёра - Режиссёр с id {} не найден", directorId);
            throw new NotFoundException("Режиссёр с id " + directorId + " не найден");
        } else {
            log.info("Режиссёр с id {} удален", directorId);
        }
    }
}
