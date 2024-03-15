package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToDirector;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToFilm;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToGenre;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary
public class JdbcFilmStorage implements FilmStorage {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcFilmStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Film findById(Long id) {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"WHERE f.id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		List<Film> result = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});

		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public List<Film> findAll() {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id";

		List<Film> result = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});

		return result;
	}

	@Override
	public Film save(Film film) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", film.getName());
		params.addValue("description", film.getDescription());
		params.addValue("releaseDate", film.getReleaseDate());
		params.addValue("duration", film.getDuration());
		params.addValue("mpaId", film.getMpa().getId());

		String sql = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) " +
				"VALUES (:name, :description, :releaseDate, :duration, :mpaId)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, params, keyHolder);
		long generatedId = keyHolder.getKey().longValue();

		film.setId(generatedId);

		updateFilmGenres(film);
		updateFilmDirector(film);

		return film;
	}

	@Override
	public Film update(Film film) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", film.getId());
		params.addValue("name", film.getName());
		params.addValue("description", film.getDescription());
		params.addValue("releaseDate", film.getReleaseDate());
		params.addValue("duration", film.getDuration());
		params.addValue("mpaId", film.getMpa().getId());

		String sql = "UPDATE films " +
				"SET name = :name, description = :description, " +
				"releaseDate = :releaseDate, duration = :duration, " +
				"mpa_id = :mpaId " +
				"WHERE id = :id";

		namedParameterJdbcTemplate.update(sql, params);

		updateFilmGenres(film);
		updateFilmDirector(film);

		return film;
	}

	@Override
	public List<Film> findBySearch(String query, String by) {
		String pattern = "%" + query + "%";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("pattern", pattern);
		params.addValue("by", by);

		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"COUNT(ufl.user_id) AS user_like_count " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN user_film_like ufl ON f.id = ufl.film_id " +
				"LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"LEFT JOIN genre g ON fg.genre_id = g.id " +
				"LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"LEFT JOIN director d ON fd.director_id = d.id " +
				"WHERE (:by = 'title' AND (LOWER(f.name) LIKE LOWER(:pattern))) " +
				"   OR (:by = 'director' AND (LOWER(d.name) LIKE LOWER(:pattern))) " +
				"   OR ((:by = 'title,director' OR :by = 'director,title') AND " +
				"       ((LOWER(f.name) LIKE LOWER(:pattern)) OR LOWER(d.name) LIKE LOWER(:pattern))) " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY user_like_count DESC";

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});
	}

	@Override
	public List<Film> getWithDirector(Long directorId, String sortBy) {
		String orderBy = "";
		switch (sortBy) {
			case "year":
				orderBy = "f.releaseDate";
				break;
			case "likes":
				orderBy = "user_like_count";
				break;
			default:
				orderBy = "fd.director_id";
				break;
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("directorId", directorId);

		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"COUNT(ufl.user_id) AS user_like_count " +
				"FROM film_director fd " +
				"LEFT JOIN films AS f ON f.id = fd.film_id " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
				"WHERE fd.director_id = :directorId " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY " + orderBy + " ASC";

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});
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
				"               WHERE (:genreId IS NULL OR fg.genre_id = :genreId) " +
				"                 AND (:year IS NULL OR YEAR(f.releaseDate) = :year)) " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY user_like_count DESC " +
				"LIMIT :count";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("genreId", genreId);
		params.addValue("year", year);
		params.addValue("count", count);

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});
	}

	@Override
	public List<Film> getCommon(Long userId, Long friendId) {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, m.name AS mpa_name " +
				"FROM FILMS f " +
				"LEFT JOIN MPA m ON f.mpa_id = m.id " +
				"WHERE f.ID IN ( " +
				"    SELECT l.FILM_ID " +
				"    FROM USER_FILM_LIKE l " +
				"    WHERE l.USER_ID = :userId " +
				"    INTERSECT " +
				"    SELECT l.FILM_ID " +
				"    FROM USER_FILM_LIKE l " +
				"    WHERE l.USER_ID = :friendId " +
				") ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);
		params.addValue("friendId", friendId);

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			return film;
		});
	}

	@Override
	public boolean deleteById(Long id) {
		String sql = "DELETE FROM films WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		return namedParameterJdbcTemplate.update(sql, params) > 0;
	}

	private List<Genre> getGenresForFilm(Long filmId) {
		String sql = "SELECT g.id, g.name " +
				"FROM genre g " +
				"INNER JOIN film_genre fg ON g.id = fg.genre_id " +
				"WHERE fg.film_id = :filmId";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);

		return namedParameterJdbcTemplate.query(sql, params, MapRowToGenre::map);
	}

	private List<Director> getDirectorsForFilm(Long filmId) {
		String sql = "SELECT d.id, d.name " +
				"FROM director d " +
				"INNER JOIN film_director fd ON d.id = fd.director_id " +
				"WHERE fd.film_id = :filmId";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);

		return namedParameterJdbcTemplate.query(sql, params, MapRowToDirector::map);
	}

	private void updateFilmGenres(Film film) {
		Long filmId = film.getId();
		List<Genre> genres = new ArrayList<>(film.getGenres());

		String sql = "DELETE FROM film_genre WHERE film_id = :filmId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);
		namedParameterJdbcTemplate.update(sql, params);

		if (!genres.isEmpty()) {
			String insertSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (:filmId, :genreId)";
			SqlParameterSource[] batchParams = genres.stream()
					.map(genre -> new MapSqlParameterSource("filmId", filmId)
							.addValue("genreId", genre.getId()))
					.toArray(SqlParameterSource[]::new);
			namedParameterJdbcTemplate.batchUpdate(insertSql, batchParams);
		}
	}

	private Film updateFilmDirector(Film film) {
		Long filmId = film.getId();
		List<Director> directors = new ArrayList<>(film.getDirectors());

		String sql = "DELETE FROM film_director WHERE film_id = :filmId";
		MapSqlParameterSource deleteParams = new MapSqlParameterSource();
		deleteParams.addValue("filmId", filmId);
		namedParameterJdbcTemplate.update(sql, deleteParams);

		if (!directors.isEmpty()) {
			String insertSql = "INSERT INTO film_director (film_id, director_id) VALUES (:filmId, :directorId)";
			SqlParameterSource[] batchParams = directors.stream()
					.map(director -> new MapSqlParameterSource("filmId", filmId)
							.addValue("directorId", director.getId()))
					.toArray(SqlParameterSource[]::new);
			namedParameterJdbcTemplate.batchUpdate(insertSql, batchParams);
		}

		return film;
	}
}
