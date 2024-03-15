package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

import java.time.LocalDate;

@Repository
public class JdbcUserFilmLikeStorage implements UserFilmLikeStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcUserFilmLikeStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void addLike(Long filmId, Long userId) {
		jdbcTemplate.update("INSERT INTO user_film_like (film_id, user_id, created_at) " +
						"VALUES (?, ?, ?)",
				filmId, userId, LocalDate.now());
	}

	@Override
	public void deleteLike(Long filmId, Long userId) {
		jdbcTemplate.update(
				"DELETE FROM user_film_like " +
						"WHERE film_id = ? " +
						"AND user_id = ?",
				filmId,
				userId
		);
	}
}
