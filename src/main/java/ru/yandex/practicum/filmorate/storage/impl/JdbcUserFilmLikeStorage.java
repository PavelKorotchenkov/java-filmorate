package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcUserFilmLikeStorage implements UserFilmLikeStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcUserFilmLikeStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Film> findPopularByGenreAndDate(int count, Integer genreId, Integer year) {
		return jdbcTemplate.query(
				"SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, mpa.name AS mpa_name, " +
						"string_agg(g.id || ',' || g.name, ';') AS genre, " +
						"string_agg(d.id || ',' || d.name, ';') AS director, " +
						"COUNT(ufl.user_id) AS user_like_count " +
						"FROM films f " +
						"LEFT JOIN mpa ON f.mpa_id = mpa.id " +
						"LEFT JOIN user_film_like ufl on f.id = ufl.film_id " +
						"LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
						"LEFT JOIN genre AS g ON fg.genre_id = g.id " +
						"LEFT JOIN film_director AS fd ON f.id = fd.film_id " +
						"LEFT JOIN director AS d ON fd.director_id = d.id" +
						" WHERE f.id IN (SELECT f.id" +
						" FROM films AS f " +
						" LEFT JOIN film_genre AS fg ON f.id = fg.film_id " +
						" WHERE (? IS NULL OR fg.genre_id = ?) " +
						" AND (? IS NULL OR YEAR(f.releaseDate) = ?)) " +
						"GROUP BY f.id " +
						"ORDER BY user_like_count DESC " +
						"LIMIT ?",
				RowMapper::mapRowToFilm, genreId, genreId, year, year, count);
	}

	@Override
	public void addLike(Long filmId, Long userId) {
		jdbcTemplate.update("INSERT INTO user_film_like (film_id, user_id, created_at) " +
						"VALUES (?, ?, ?)",
				filmId, userId, LocalDate.now());
	}

	@Override
	public void deleteLike(Long filmId, Long userId) {
		jdbcTemplate.update(
				"DELETE FROM user_film_like " +
						"WHERE film_id = ? " +
						"AND user_id = ?",
				filmId,
				userId
		);
	}

	@Override
	public List<Film> getCommon(Long userId, Long friendId) {
		String sqlQuery = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, m.name AS mpa_name, " +
				"string_agg(G2.id || ',' || G2.name, ';') AS genre, " +
				"string_agg(D.id || ',' || D.name, ';') AS director " +
				"FROM FILMS f " +
				"         LEFT JOIN FILM_GENRE FG ON f.ID = FG.FILM_ID " +
				"         LEFT JOIN MPA M ON M.ID = f.MPA_ID " +
				"         LEFT JOIN GENRE G2 ON FG.GENRE_ID = G2.ID " +
				"         LEFT JOIN film_director fd ON f.id = fd.film_id " +
				"         LEFT JOIN director D ON fd.director_id = D.id " +
				"WHERE f.ID IN ( " +
				"    SELECT l.FILM_ID " +
				"    FROM USER_FILM_LIKE l " +
				"    WHERE l.USER_ID = ? " +
				"    INTERSECT  " +
				"    SELECT l.FILM_ID " +
				"    FROM USER_FILM_LIKE l " +
				"    WHERE l.USER_ID = ?" +
				"     ) " +
				" GROUP BY f.ID;";
		return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFilm, userId, friendId);
	}
}
