package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
public class UserFilmLikeDbStorage implements UserFilmLikeStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserFilmLikeDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Film> findPopular(int count) {
		log.info("Находим топ {} популярных фильмов", count);
		return jdbcTemplate.query(
				"SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
						"string_agg(g.id || ',' || g.name, ';') AS genre, " +
						"COUNT(ufl.user_id) AS user_like_count " +
						"FROM films f " +
						"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
						"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
						"LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
						"LEFT JOIN genre AS g ON fg.genre_id = g.id " +
						"GROUP BY f.id " +
						"ORDER BY user_like_count DESC " +
						"LIMIT ?",
				RowMapper::mapRowToFilm,
				count);
	}

	/*@Override
	public void addLike(Long filmId, Long userId) {
		log.info("Добавляем лайк фильму {} от юзера {}", filmId, userId);
		jdbcTemplate.update("INSERT INTO user_film_like (film_id, user_id, created_at) " +
						"VALUES (?, ?, ?)",
				filmId, userId, LocalDate.now());
	}*/

	@Override
	public void addLike(Long filmId, Long userId) {
		log.info("Добавляем лайк фильму {} от юзера {}", filmId, userId);
		jdbcTemplate.update("INSERT INTO user_film_like (film_id, user_id) " +
						"VALUES (?, ?)",
				filmId, userId);
	}

	@Override
	public void deleteLike(Long filmId, Long userId) {
		log.info("Удаляем лайк фильму {} от юзера {}", filmId, userId);
		jdbcTemplate.update(
				"DELETE FROM user_film_like " +
						"WHERE film_id = ? " +
						"AND user_id = ?",
				filmId,
				userId
		);
	}
}
