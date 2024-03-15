package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToDirector;

import java.util.List;

@Repository
@Primary
public class JdbcDirectorStorage implements DirectorStorage {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcDirectorStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Director> getAll() {
		String sql = "SELECT * FROM director ORDER by id";
		return namedParameterJdbcTemplate.query(
				sql,
				MapRowToDirector::map);
	}

	@Override
	public Director getById(Long directorId) {
		String sql = "SELECT * FROM director WHERE id = :directorId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("directorId", directorId);

		List<Director> directors = namedParameterJdbcTemplate.query(
				sql,
				params,
				MapRowToDirector::map);

		return directors.isEmpty() ? null : directors.get(0);
	}

	@Override
	public Director create(Director director) {
		String sql = "INSERT INTO director (name) VALUES (:name)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", director.getName());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, params, keyHolder);

		Long generatedId = keyHolder.getKey().longValue();
		director.setId(generatedId);

		return director;
	}

	@Override
	public Director update(Director director) {
		String sql = "UPDATE director SET name = :name WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", director.getName());
		params.addValue("id", director.getId());
		namedParameterJdbcTemplate.update(sql, params);
		return director;
	}

	@Override
	public void delete(Long directorId) {
		String sql = "DELETE FROM director WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", directorId);
		namedParameterJdbcTemplate.update(sql, params);
	}
}
