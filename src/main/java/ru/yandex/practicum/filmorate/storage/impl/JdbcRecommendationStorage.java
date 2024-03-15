package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.RecommendationStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToDirector;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToFilm;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToGenre;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToUser;

import java.util.*;

@Component
public class JdbcRecommendationStorage implements RecommendationStorage {

	private final JdbcTemplate jdbcTemplate;

	public JdbcRecommendationStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private Optional<User> getUserMaxOverlapLikes(long id) {
		String sql = "SELECT id, email, name, login, birthday FROM USERS u WHERE u.ID = (SELECT user_id\n" +
				"FROM user_film_like\n" +
				"WHERE film_id IN (\n" +
				"    SELECT film_id\n" +
				"    FROM user_film_like\n" +
				"    WHERE user_id = ?\n" +
				") AND user_id != ?\n" +
				"GROUP BY user_id\n" +
				"ORDER BY COUNT(*) DESC\n" +
				"LIMIT 1); ";

		List<User> users = jdbcTemplate.query(sql, MapRowToUser::map, id, id);
		if (users.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(users.get(0));
		}
	}

	@Override
	public List<Film> getById(long id) {
		Optional<User> overlapUser = getUserMaxOverlapLikes(id);
		if (overlapUser.isEmpty()) {
			return new ArrayList<>();
		}

		String sql = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name " +
				"FROM FILMS f " +
				"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
				"WHERE f.id IN (" +
				"    SELECT DISTINCT a.film_id " +
				"    FROM user_film_like AS a " +
				"    WHERE a.user_id = ? " +
				"    AND a.film_id NOT IN (" +
				"        SELECT film_id " +
				"        FROM user_film_like " +
				"        WHERE user_id = ?" +
				"    )" +
				")";

		List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		}, overlapUser.get().getId(), id);

		return films;
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
}
