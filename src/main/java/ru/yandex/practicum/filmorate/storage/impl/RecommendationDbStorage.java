package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RecommendationDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Optional<User> getUserMaxOverlapLikes(long id) {
        String sql = "SELECT id, email, name, login, birthday FROM USERS u WHERE u.ID = (SELECT user_id\n" +
                "FROM user_film_like\n" +
                "WHERE film_id IN (\n" +
                "    SELECT film_id\n" +
                "    FROM user_film_like\n" +
                "    WHERE user_id = ?\n" +
                ") AND user_id != ?\n" +
                "GROUP BY user_id\n" +
                "ORDER BY COUNT(*) DESC\n" +
                "LIMIT 1); ";

        List<User> users = jdbcTemplate.query(sql, RowMapper::mapRowToUser, id, id);
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(users.get(0));
        }
    }

    public List<Film> getRecommendation(long id) {
        Optional<User> overlapUser = getUserMaxOverlapLikes(id);
        if (overlapUser.isEmpty()) return new ArrayList<>();

        String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, " +
                "mpa.name AS mpa_name, string_agg(g.id || ',' || g.name, ';') AS genre \n" +
                "FROM FILMS f \n" +
                "LEFT JOIN mpa ON f.mpa_id = mpa.id\n" +
                "LEFT JOIN film_genre AS fg ON f.id = fg.film_id\n" +
                "LEFT JOIN genre AS g ON fg.genre_id = g.id\n" +
                "WHERE f.id IN (\n" +
                "    SELECT DISTINCT a.film_id\n" +
                "    FROM user_film_like AS a\n" +
                "    WHERE a.user_id = ? \n" +
                "    AND a.film_id NOT IN (\n" +
                "        SELECT film_id\n" +
                "        FROM user_film_like\n" +
                "        WHERE user_id = ?\n" +
                "    )\n" +
                ")\n" +
                "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name;";

        return jdbcTemplate.query(sql, RowMapper::mapRowToFilm, overlapUser.get().getId(), id);
    }
}