package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToDirector;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToFilm;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToGenre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
@Primary
public class JdbcFilmStorage implements FilmStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcFilmStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Film findById(Long id) {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"WHERE f.id = ?";

		List<Film> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		}, id);

		return result.isEmpty() ? null : result.get(0);
	}

	public List<Film> findAll() {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});
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

		updateFilmGenres(film);
		updateFilmDirector(film);

		return film;
	}

	@Override
	public Film update(Film film) {
		Long id = film.getId();
		jdbcTemplate.update("UPDATE films " +
						"SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? " +
						"WHERE id = ?",
				film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), id);

		updateFilmGenres(film);
		updateFilmDirector(film);

		return film;
	}

	@Override
	public List<Film> findBySearch(String query, String by) {
		String pattern = "%" + query + "%";
		String sqlQuery = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"COUNT(ufl.user_id) AS user_like_count " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN user_film_like ufl ON f.id = ufl.film_id " +
				"LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"LEFT JOIN genre g ON fg.genre_id = g.id " +
				"LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"LEFT JOIN director d ON fd.director_id = d.id " +
				"WHERE (? = 'title' AND (LOWER(f.name) LIKE LOWER(?))) " +
				"   OR (? = 'director' AND (LOWER(d.name) LIKE LOWER(?))) " +
				"   OR ((? = 'title,director' OR ? = 'director,title') AND " +
				"       ((LOWER(f.name) LIKE LOWER(?)) OR LOWER(d.name) LIKE LOWER(?))) " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY user_like_count DESC";

		return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		}, by, pattern, by, pattern, by, by, pattern, pattern);
	}

	@Override
	public List<Film> getWithDirector(Long directorId, String sortBy) {
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

		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"COUNT(ufl.user_id) AS user_like_count " +
				"FROM film_director fd " +
				"LEFT JOIN films AS f ON f.id = fd.film_id " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
				"WHERE fd.director_id = ? " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY " + orderBy + " ASC";

		List<Film> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		}, directorId);

		return result;
	}

	@Override
	public List<Film> findPopularByGenreAndDate(int count, Long genreId, Integer year) {

		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"COUNT(ufl.user_id) AS user_like_count " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
				"LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
				"WHERE f.id IN (SELECT f.id " +
				"               FROM films AS f " +
				"               LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
				"               WHERE (? IS NULL OR fg.genre_id = ?) " +
				"                 AND (? IS NULL OR YEAR(f.releaseDate) = ?)) " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY user_like_count DESC " +
				"LIMIT ?";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		}, genreId, genreId, year, year, count);
	}

	@Override
	public List<Film> getCommon(Long userId, Long friendId) {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, m.name AS mpa_name " +
				"FROM FILMS f " +
				"LEFT JOIN MPA m ON f.mpa_id = m.id " +
				"WHERE f.ID IN ( " +
				"    SELECT l.FILM_ID " +
				"    FROM USER_FILM_LIKE l " +
				"    WHERE l.USER_ID = ? " +
				"    INTERSECT " +
				"    SELECT l.FILM_ID " +
				"    FROM USER_FILM_LIKE l " +
				"    WHERE l.USER_ID = ? " +
				") ";

		List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			return film;
		}, userId, friendId);
		return films;
	}

	@Override
	public boolean deleteById(Long id) {
		String sqlQuery = "DELETE FROM FILMS WHERE ID = ?";
		return jdbcTemplate.update(sqlQuery, id) > 0;
	}

	private List<Genre> getGenresForFilm(Long filmId) {
		String sql = "SELECT g.id, g.name " +
				"FROM genre g " +
				"INNER JOIN film_genre fg ON g.id = fg.genre_id " +
				"WHERE fg.film_id = ?";

		return jdbcTemplate.query(sql, new Object[]{filmId}, MapRowToGenre::map);
	}

	private List<Director> getDirectorsForFilm(Long filmId) {
		String sql = "SELECT d.id, d.name " +
				"FROM director d " +
				"INNER JOIN film_director fd ON d.id = fd.director_id " +
				"WHERE fd.film_id = ?";

		return jdbcTemplate.query(sql, new Object[]{filmId}, MapRowToDirector::map);
	}

	private void updateFilmGenres(Film film) {
		jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?",
				film.getId());

		List<Genre> genres = new ArrayList<>(film.getGenres());
		Long filmId = film.getId();

		if (film.getGenres().isEmpty()) {
			return;
		}

		jdbcTemplate.batchUpdate("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, filmId);
						ps.setLong(2, genres.get(i).getId());
					}

					@Override
					public int getBatchSize() {
						return genres.size();
					}
				});
	}

	private Film updateFilmDirector(Film film) {
		jdbcTemplate.update("DELETE FROM film_director WHERE film_id = ?",
				film.getId());

		List<Director> directors = new ArrayList<>(film.getDirectors());
		Long filmId = film.getId();

		jdbcTemplate.batchUpdate("INSERT INTO film_director (film_id, director_id) VALUES (?, ?)",
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, filmId);
						ps.setLong(2, directors.get(i).getId());
					}

					@Override
					public int getBatchSize() {
						return directors.size();
					}
				});

		return film;
	}
}
