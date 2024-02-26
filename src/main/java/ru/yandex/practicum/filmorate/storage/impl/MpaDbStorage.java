package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.util.List;

@Slf4j
@Repository
@Component
public class MpaDbStorage implements MpaStorage {
	private final JdbcTemplate jdbcTemplate;

	public MpaDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Mpa> findAllMpas() {
		log.info("Запрос всех возрастных рейтингов");
		return jdbcTemplate.query(
				"SELECT * " +
						"FROM mpa " +
						"ORDER BY id",
				RowMapper::mapRowToMpa);
	}

	@Override
	public Mpa findMpaById(Long id) {
		log.info("Запрос возрастного рейтинга по id");
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
