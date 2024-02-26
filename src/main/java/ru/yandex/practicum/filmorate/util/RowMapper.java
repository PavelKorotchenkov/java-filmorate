package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

		Set<Genre> genres = new HashSet<>();

		for (Genre genre : film.getGenres()) {
			genres.add(findGenreById(genre.getId()));
		}

		film.setGenres(genres);
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
}
