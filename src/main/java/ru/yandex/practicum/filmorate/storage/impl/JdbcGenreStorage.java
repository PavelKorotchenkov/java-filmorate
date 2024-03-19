package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToGenre;

import java.util.List;

@Repository
public class JdbcGenreStorage implements GenreStorage {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public JdbcGenreStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Genre> findAll() {
		String sql = "SELECT * FROM genre ORDER BY id";
		return namedParameterJdbcTemplate.query(sql, MapRowToGenre::map);
	}

	@Override
	public Genre findById(Long id) {
		String sql = "SELECT * FROM genre WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		List<Genre> genres = namedParameterJdbcTemplate.query(sql, params, MapRowToGenre::map);
		return genres.isEmpty() ? null : genres.get(0);
	}
}
