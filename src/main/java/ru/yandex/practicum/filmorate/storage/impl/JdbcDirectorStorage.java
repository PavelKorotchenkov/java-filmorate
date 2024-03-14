package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@Primary
public class JdbcDirectorStorage implements DirectorStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcDirectorStorage(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Director> getAll() {
        return namedParameterJdbcTemplate.query(
                "SELECT * " +
                        "FROM director " +
                        "ORDER by id",
                RowMapper::mapRowToDirector);
    }

    @Override
    public Optional<Director> getById(Long directorId) {
        List<Director> directors = namedParameterJdbcTemplate.query(
                "SELECT * " +
                        "FROM director " +
                        "WHERE id = :id",
                new MapSqlParameterSource("id", directorId),
                RowMapper::mapRowToDirector);

        if (directors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(directors.get(0));
        }
    }

    @Override
    public Director create(Director director) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO director (name) VALUES (:name)";

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource(toMap(director)),
                generatedKeyHolder);

        director.setId(generatedKeyHolder.getKey().longValue());

        return director;
    }

    @Override
    public Optional<Director> update(Director director) {
        Optional<Director> result = getById(director.getId());
        if (result.isPresent()) {
            String sqlQuery = "UPDATE director SET " +
                    "name = :name " +
                    "WHERE id = :id";
            namedParameterJdbcTemplate.update(
                    sqlQuery,
                    new MapSqlParameterSource(toMap(director)));
            return Optional.of(director);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long directorId) {
        String sqlQuery = "DELETE FROM director WHERE id = :id";
        namedParameterJdbcTemplate.update(
                sqlQuery,
                new MapSqlParameterSource("id",
                        directorId));
    }

    public Map<String, Object> toMap(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", director.getId());
        values.put("name", director.getName());
        return values;
    }
}
