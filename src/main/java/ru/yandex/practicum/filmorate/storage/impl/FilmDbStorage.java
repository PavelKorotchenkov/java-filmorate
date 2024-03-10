package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public FilmDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Film findFilmById(Long id) {
		List<Film> result = jdbcTemplate.query(
				"SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
						"string_agg(g.id || ',' || g.name, ';') AS genre, " +
						"string_agg(d.id || ',' || d.name, ';') AS director " +
						"FROM films f " +
						"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
						"LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
						"LEFT JOIN genre AS g ON fg.genre_id = g.id " +
						"LEFT JOIN film_director AS fd ON f.id = fd.film_id " +
						"LEFT JOIN director AS d ON fd.director_id = d.id " +
						"WHERE f.id = ? " +
						"GROUP BY f.id,f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name;",
				RowMapper::mapRowToFilm,
				id
		);
		if (result.size() != 1) {
			log.info("Фильм с идентификатором {} не найден.", id);
			throw new NotFoundException("Фильм с id " + id + " не найден.");
		}

		Film film = result.get(0);
		log.info("Найден фильм с id: {}", id);
		return film;
	}

	@Override
	public List<Film> findAllFilms() {
		log.info("Запрос всех фильмов");
		List<Film> films = jdbcTemplate.query(
				"SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
						"string_agg(g.id || ',' || g.name, ';') AS genre, " +
						"string_agg(d.id || ',' || d.name, ';') AS director " +
						"FROM films f " +
						"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
						"LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
						"LEFT JOIN genre AS g ON fg.genre_id = g.id " +
						"LEFT JOIN film_director AS fd ON f.id = fd.film_id " +
						"LEFT JOIN director AS d ON fd.director_id = d.id " +
						"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name;",
				RowMapper::mapRowToFilm);
		return films;
	}

	@Override
	public Film save(Film film) {
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?, ?, ?, ?, ?)",
				Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER);
		pscf.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc =
				pscf.newPreparedStatementCreator(
						Arrays.asList(
								film.getName(),
								film.getDescription(),
								film.getReleaseDate(),
								film.getDuration(),
								film.getMpa().getId()));
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		long orderId = keyHolder.getKey().longValue();
		film.setId(orderId);
		log.info("В базу добавлен новый фильм: " + film);

		updateFilmGenres(film, film.getGenres());
		updateFilmDirector(film);

		return film;
	}

	@Override
	public Film update(Film film) {
		Long id = film.getId();
		findFilmById(id); //проверяем, что фильм есть в базе
		jdbcTemplate.update("UPDATE films " +
						"SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? " +
						"WHERE id = ?",
				film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), id);

		log.info("Обновлен фильм: " + film);

		updateFilmGenres(film, film.getGenres()); //вносим строку фильм-жанр в связанную таблицу
		film.setGenres(getGenres(film.getId()));  //получаем названия жанров, обновляем жанры в Film
		updateFilmDirector(film); //вносим строку фильм-режиссёр в связанную таблицу

		return film;
	}

	@Override
	public List<Film> findFilmBySearch(String query, String by) {
		String pattern = "%" + query + "%";
		String sqlQuery = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"string_agg(g.id || ',' || g.name, ';') AS genre, " +
				"string_agg(d.id || ',' || d.name, ';') AS director, " +
				"COUNT(ufl.user_id) AS user_like_count " +
				"FROM films f\n" +
				"         LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"         LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"         LEFT JOIN genre g ON fg.genre_id = g.id " +
				"         LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"         LEFT JOIN director d ON fd.director_id = d.id " +
				"         LEFT JOIN user_film_like ufl ON f.id = ufl.film_id " +
				"WHERE ('title' = ? AND (LOWER(f.name) LIKE LOWER(?))) " +
				"   OR ('director' = ? AND (LOWER(d.name) LIKE LOWER(?))) " +
				"   OR (('title,director' = ? OR 'director,title' = ?) AND " +
				"       ((LOWER(f.name) LIKE LOWER(?)) OR LOWER(d.name) LIKE LOWER(?))) " +
				"GROUP BY f.id " +
				"ORDER BY user_like_count DESC;";
		return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm, by, pattern, by, pattern, by, by, pattern, pattern);
	}

	@Override
	public List<Film> getFilmsWithDirector(Long directorId, String sortBy) {
		String orderBy = "";
		switch (sortBy) {
			case ("year"):
				orderBy = "f.releaseDate";
				break;
			case ("likes"):
				orderBy = "user_like_count";
				break;
			default:
				orderBy = "fd.director_id";
				break;
		}

		List<Film> result = jdbcTemplate.query(
				"SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
						"string_agg(g.id || ',' || g.name, ';') AS genre, " +
						"string_agg(d.id || ',' || d.name, ';') AS director, " +
						"COUNT(ufl.user_id) AS user_like_count " +
						"FROM film_director fd " +
						"LEFT JOIN films AS f ON f.id = fd.film_id " +
						"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
						"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
						"LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
						"LEFT JOIN genre AS g ON fg.genre_id = g.id " +
						"LEFT JOIN director AS d ON fd.director_id = d.id " +
						"WHERE fd.director_id = ? " +
						"GROUP BY f.id " +
						"ORDER BY " + orderBy + " ASC;",
				RowMapper::mapRowToFilm,
				directorId
		);
		if (result.isEmpty()) {
			log.info("Режиссёр с идентификатором {} не найден.", directorId);
			throw new NotFoundException("Режиссёр с id " + directorId + " не найден.");
		}
		return result;
	}

	private Set<Genre> getGenres(Long id) {
		List<Genre> genres = jdbcTemplate.query(
				"SELECT fg.film_id, fg.genre_id AS id, g.name AS name " +
						"FROM film_genre fg " +
						"LEFT JOIN genre g ON fg.genre_id = g.id " +
						"WHERE film_id = ? " +
						"ORDER BY id",
				RowMapper::mapRowToGenre,
				id
		);
		return new HashSet<>(genres);
	}

	private void updateFilmGenres(Film film, Set<Genre> genres) {
		jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?",
				film.getId());

		if (genres.isEmpty()) {
			return;
		}

		for (Genre genre : genres) {
			jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
					film.getId(), genre.getId());
			log.info("К фильму {} добавлен новый жанр {} в связанную таблицу film_genre", film, genre);
		}
	}

	public Film updateFilmDirector(Film film) {
		jdbcTemplate.update("DELETE FROM film_director WHERE film_id = ?",
				film.getId());
		for (Director director : film.getDirectors()) {
			jdbcTemplate.update("INSERT INTO film_director (film_id, director_id) VALUES (?, ?)",
					film.getId(), director.getId());
			log.info("К фильму {} добавлен режиссёр {} в связанную таблицу film_director", film, director);
		}
		return film;
	}
	
    @Override
    public boolean deleteById(Long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }
}
