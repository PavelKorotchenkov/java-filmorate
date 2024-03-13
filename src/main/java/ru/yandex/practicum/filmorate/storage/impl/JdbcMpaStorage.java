package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.util.List;

@Repository
public class JdbcMpaStorage implements MpaStorage {
	private final JdbcTemplate jdbcTemplate;

	public JdbcMpaStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Mpa> findAll() {
		return jdbcTemplate.query(
				"SELECT * " +
						"FROM mpa " +
						"ORDER BY id",
				RowMapper::mapRowToMpa);
	}

	@Override
	public Mpa findById(Long id) {
		List<Mpa> mpa = jdbcTemplate.query(
				"SELECT * " +
						"FROM mpa " +
						"WHERE id = ?",
				RowMapper::mapRowToMpa,
				id);

		if (mpa.isEmpty()) {
			throw new NotFoundException("Возрастной рейтинг с идентификатором + " + id + " не найден.");
		}

		return mpa.get(0);
	}
}
