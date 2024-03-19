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

import java.util.*;

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
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"g.id AS genre_id, g.name AS genre_name, " +
				"d.id AS director_id, d.name AS director_name " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"LEFT JOIN genre g ON fg.genre_id = g.id " +
				"LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"LEFT JOIN director d ON fd.director_id = d.id " +
				"WHERE f.id = :id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		Map<Long, Film> filmMap = new LinkedHashMap<>();

		List<Film> result = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Long filmId = rs.getLong("id");
			Film film = filmMap.get(filmId);
			if (film == null) {
				film = MapRowToFilm.map(rs, rowNum);
				filmMap.put(filmId, film);
			} else {
				Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
				film.getGenres().add(genre);

				long directorId = rs.getLong("director_id");
				if (directorId != 0) {
					Director director = new Director(rs.getLong("director_id"), rs.getString("director_name"));
					film.getDirectors().add(director);
				}
			}

			return film;
		});

		return filmMap.get(id);
	}

	@Override
	public List<Film> findAll() {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
				"g.id AS genre_id, g.name AS genre_name, " +
				"d.id AS director_id, d.name AS director_name " +
				"FROM films f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"LEFT JOIN genre g ON fg.genre_id = g.id " +
				"LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"LEFT JOIN director d ON fd.director_id = d.id";

		Map<Long, Film> filmMap = new HashMap<>();

		List<Film> result = namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> {
			Long filmId = rs.getLong("id");
			Film film = filmMap.get(filmId);
			Film dbFilm = MapRowToFilm.map(rs, rowNum);

			if (film == null) {
				filmMap.put(filmId, dbFilm);
			} else {
				film.getGenres().addAll(dbFilm.getGenres());
				film.getDirectors().addAll(dbFilm.getDirectors());
			}

			return film;
		});
		return new ArrayList<>(filmMap.values());
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
				"g.id AS genre_id, g.name AS genre_name, " +
				"d.id AS director_id, d.name AS director_name, " +
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

		Map<Long, Film> filmMap = new LinkedHashMap<>();

		List<Film> result = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Long filmId = rs.getLong("id");
			Film film = filmMap.get(filmId);
			Film dbFilm = MapRowToFilm.map(rs, rowNum);

			if (film == null) {
				filmMap.put(filmId, dbFilm);
			} else {
				film.getGenres().addAll(dbFilm.getGenres());
				film.getDirectors().addAll(dbFilm.getDirectors());
			}

			return film;
		});
		return new ArrayList<>(filmMap.values());
	}

	@Override
	public List<Film> getWithDirector(Long directorId, String sortBy) {
		String orderBy;
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
				"COUNT(ufl.user_id) AS user_like_count, " +
				"g.id AS genre_id, g.name AS genre_name, " +
				"d.id AS director_id, d.name AS director_name, " +
				"FROM film_director fd " +
				"LEFT JOIN films AS f ON f.id = fd.film_id " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
				"LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"LEFT JOIN genre g ON fg.genre_id = g.id " +
				"LEFT JOIN director d ON fd.director_id = d.id " +
				"WHERE fd.director_id = :directorId " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY " + orderBy + " ASC";

		Map<Long, Film> filmMap = new LinkedHashMap<>();

		List<Film> result = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Long filmId = rs.getLong("id");
			Film film = filmMap.get(filmId);
			Film dbFilm = MapRowToFilm.map(rs, rowNum);

			if (film == null) {
				filmMap.put(filmId, dbFilm);
			} else {
				film.getGenres().addAll(dbFilm.getGenres());
				film.getDirectors().addAll(dbFilm.getDirectors());
			}

			return film;
		});
		return new ArrayList<>(filmMap.values());
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
				"               AND (:year IS NULL OR YEAR(f.releaseDate) = :year)) " +
				"GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa_name " +
				"ORDER BY user_like_count DESC " +
				"LIMIT :count";

		String sqlFilmGenres = "SELECT fg.film_id, fg.genre_id AS id, g.name AS name " +
				"FROM film_genre fg " +
				"LEFT JOIN genre g ON fg.genre_id = g.id";

		String sqlFilmDirectors = "SELECT fd.film_id AS film_id, fd.director_id AS id, d.name AS name " +
				"FROM film_director fd " +
				"LEFT JOIN director d ON fd.director_id = d.id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("genreId", genreId);
		params.addValue("year", year);
		params.addValue("count", count);

		Map<Long, List<Genre>> genresMap = new LinkedHashMap<>();
		Map<Long, List<Director>> directorsMap = new LinkedHashMap<>();

		List<Genre> genres = namedParameterJdbcTemplate.query(sqlFilmGenres, (rs, rowNum) -> {
			Long filmId = rs.getLong("film_id");
			Genre genre = MapRowToGenre.map(rs, rowNum);
			if (genresMap.containsKey(filmId)) {
				List<Genre> genreList = genresMap.get(filmId);
				genreList.add(genre);
				genresMap.replace(filmId, genreList);
			} else {
				genresMap.put(filmId, new ArrayList<>(List.of(genre)));
			}
			return genre;
		});

		List<Director> directors = namedParameterJdbcTemplate.query(sqlFilmDirectors, (rs, rowNum) -> {
			Long filmId = rs.getLong("film_id");
			Director director = MapRowToDirector.map(rs, rowNum);
			if (directorsMap.containsKey(filmId)) {
				List<Director> directorList = directorsMap.get(filmId);
				directorList.add(director);
				directorsMap.put(filmId, directorList);
			} else {
				directorsMap.put(filmId, new ArrayList<>(List.of(director)));
			}
			return director;
		});

		Map<Long, Film> filmMap = new LinkedHashMap<>();

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film dbFilm = MapRowToFilm.map(rs, rowNum);
			if (genresMap.containsKey(dbFilm.getId())) {
				dbFilm.setGenres(new LinkedHashSet<>(genresMap.get(dbFilm.getId())));
			}
			if (directorsMap.containsKey(dbFilm.getId())) {
				dbFilm.setDirectors(new LinkedHashSet<>(directorsMap.get(dbFilm.getId())));
			}
			return dbFilm;
		});
	}

	@Override
	public List<Film> getCommon(Long userId, Long friendId) {
		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, m.name AS mpa_name, " +
				"g.id AS genre_id, g.name AS genre_name, " +
				"d.id AS director_id, d.name AS director_name " +
				"FROM FILMS f " +
				"LEFT JOIN MPA m ON f.mpa_id = m.id " +
				"LEFT JOIN film_genre fg ON f.id = fg.film_id " +
				"LEFT JOIN genre g ON fg.genre_id = g.id " +
				"LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"LEFT JOIN director d ON fd.director_id = d.id " +
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

		Map<Long, Film> filmMap = new HashMap<>();

		List<Film> result = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Long filmId = rs.getLong("id");
			Film film = filmMap.get(filmId);
			Film dbFilm = MapRowToFilm.map(rs, rowNum);

			if (film == null) {
				filmMap.put(filmId, dbFilm);
			} else {
				film.getGenres().addAll(dbFilm.getGenres());
				film.getDirectors().addAll(dbFilm.getDirectors());
			}

			return film;
		});
		return new ArrayList<>(filmMap.values());
	}

	@Override
	public boolean deleteById(Long id) {
		String sql = "DELETE FROM films WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		return namedParameterJdbcTemplate.update(sql, params) > 0;
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

	private void updateFilmDirector(Film film) {
		Long filmId = film.getId();
		List<Director> directors = new ArrayList<>(film.getDirectors());

		String sql = "DELETE FROM film_director WHERE film_id = :filmId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);
		namedParameterJdbcTemplate.update(sql, params);

		if (!directors.isEmpty()) {
			String insertSql = "INSERT INTO film_director (film_id, director_id) VALUES (:filmId, :directorId)";
			SqlParameterSource[] batchParams = directors.stream()
					.map(director -> new MapSqlParameterSource("filmId", filmId)
							.addValue("directorId", director.getId()))
					.toArray(SqlParameterSource[]::new);
			namedParameterJdbcTemplate.batchUpdate(insertSql, batchParams);
		}
	}
}
