package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToMpa;

import java.util.List;

@Repository
public class JdbcMpaStorage implements MpaStorage {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public JdbcMpaStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Mpa> findAll() {
		String sql = "SELECT * FROM mpa ORDER BY id";
		return namedParameterJdbcTemplate.query(sql, MapRowToMpa::map);
	}

	@Override
	public Mpa findById(Long id) {
		String sql = "SELECT * FROM mpa WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		List<Mpa> mpa = namedParameterJdbcTemplate.query(sql, params, MapRowToMpa::map);
		return mpa.isEmpty() ? null : mpa.get(0);
	}
}
