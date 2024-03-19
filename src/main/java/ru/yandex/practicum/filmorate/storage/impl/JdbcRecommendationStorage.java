package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public JdbcRecommendationStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
				"    WHERE a.user_id = :overlapUserId " +
				"    AND a.film_id NOT IN (" +
				"        SELECT film_id " +
				"        FROM user_film_like " +
				"        WHERE user_id = :userId" +
				"    )" +
				")";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("overlapUserId", overlapUser.get().getId());
		params.addValue("userId", id);

		return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
			Film film = MapRowToFilm.map(rs, rowNum);

			Set<Genre> genres = new LinkedHashSet<>(getGenresForFilm(film.getId()));
			film.setGenres(genres);

			Set<Director> directors = new LinkedHashSet<>(getDirectorsForFilm(film.getId()));
			film.setDirectors(directors);

			return film;
		});
	}

	private Optional<User> getUserMaxOverlapLikes(long id) {
		String sql = "SELECT id, email, name, login, birthday FROM USERS u WHERE u.ID = (SELECT user_id " +
				"FROM user_film_like " +
				"WHERE film_id IN ( " +
				"    SELECT film_id " +
				"    FROM user_film_like " +
				"    WHERE user_id = :id " +
				") AND user_id != :id " +
				"GROUP BY user_id " +
				"ORDER BY COUNT(*) DESC " +
				"LIMIT 1); ";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		List<User> users = namedParameterJdbcTemplate.query(sql, params, MapRowToUser::map);
		if (users.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(users.get(0));
		}
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
}
