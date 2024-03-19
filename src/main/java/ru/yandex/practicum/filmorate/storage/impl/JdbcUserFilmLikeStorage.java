package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

@Repository
public class JdbcUserFilmLikeStorage implements UserFilmLikeStorage {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcUserFilmLikeStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void addLike(Long filmId, Long userId) {
		String sql = "INSERT INTO user_film_like (film_id, user_id) VALUES (:filmId, :userId)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);
		params.addValue("userId", userId);
		namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public void deleteLike(Long filmId, Long userId) {
		String sql = "DELETE FROM user_film_like WHERE film_id = :filmId AND user_id = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);
		params.addValue("userId", userId);
		namedParameterJdbcTemplate.update(sql, params);
	}
}
