package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class RowMapper {
	public static User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String email = row.getString("email");
		String login = row.getString("login");
		String name = row.getString("name");
		LocalDate birthday = row.getDate("birthday").toLocalDate();
		return new User(id, email, login, name, birthday);
	}

	public static Friendship mapRowToFriendship(ResultSet row, int rowNum) throws SQLException {
		Long userId = row.getLong("user_id");
		Long friendId = row.getLong("friend_id");
		Boolean status = row.getBoolean("friendship_status");
		return new Friendship(userId, friendId, status);
	}

	public static Film mapRowToFilm(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		String description = row.getString("description");
		LocalDate releaseDate = row.getDate("releaseDate").toLocalDate();
		long duration = row.getLong("duration");
		long mpaId = row.getInt("mpa_id");
		String mpaName = row.getString("mpa_name");
		Mpa mpa = new Mpa(mpaId, mpaName);
		Film film = new Film(id, name, description, releaseDate, duration, mpa);

		String genreRowData = row.getString("genre");
		Set<Genre> genreSet = new HashSet<>();

		if (genreRowData != null && !genreRowData.isEmpty() && !genreRowData.isBlank()) {
			String[] genreRow = genreRowData.split(";");
			for (String s : genreRow) {
				String[] finalGenre = s.split(",");
				long genreId = Long.parseLong(finalGenre[0]);
				String genreName = finalGenre[1];
				Genre genre = new Genre(genreId, genreName);
				genreSet.add(genre);
			}
		}
		film.setGenres(genreSet);
		return film;
	}

	public static Genre mapRowToGenre(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		return new Genre(id, name);
	}

	public static Mpa mapRowToMpa(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		return new Mpa(id, name);
	}

	public static Review mapRowToReview(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String content = row.getString("content");
		boolean isPositive = row.getBoolean("isPositive");
		Long userId = row.getLong("user_id");
		Long filmId = row.getLong("film_id");
		int useful = row.getInt("useful");

		return new Review(id, content, isPositive, userId, filmId, useful);
	}

	public static ReviewLike mapRowToReviewLike(ResultSet row, int rowNum) throws SQLException {
		Long reviewId = row.getLong("review_id");
		Long userId = row.getLong("user_id");
		boolean isPositive = row.getBoolean("isPositive");

		return new ReviewLike(reviewId, userId, isPositive);
	}
}
