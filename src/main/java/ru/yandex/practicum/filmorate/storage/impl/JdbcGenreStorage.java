package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToGenre;

import java.util.List;

@Repository
public class JdbcGenreStorage implements GenreStorage {

	private final JdbcTemplate jdbcTemplate;

	public JdbcGenreStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Genre> findAll() {
		return jdbcTemplate.query(
				"SELECT * " +
						"FROM genre " +
						"ORDER by id",
				MapRowToGenre::map);
	}

	@Override
	public Genre findById(Long id) {
		List<Genre> genres = jdbcTemplate.query(
				"SELECT * " +
						"FROM genre " +
						"WHERE id = ?",
				MapRowToGenre::map,
				id);

		return genres.isEmpty() ? null : genres.get(0);
	}
}
