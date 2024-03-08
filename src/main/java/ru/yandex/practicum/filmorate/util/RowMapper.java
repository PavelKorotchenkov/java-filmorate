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

		Film film = new Film(id, name, description, releaseDate, duration, mpa, genreSet);

		String directorRowData = row.getString("director");
		Set<Director> directorSet = new HashSet<>();

		if (directorRowData != null && !directorRowData.isEmpty() && !directorRowData.isBlank()) {
			String[] directorRow = directorRowData.split(";");
			for (String s : directorRow) {
				String[] finalDirector = s.split(",");
				long directorId = Long.parseLong(finalDirector[0]);
				String directorName = finalDirector[1];
				Director director = new Director(directorId, directorName);
				directorSet.add(director);
			}
		}
		film.setDirectors(directorSet);
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

	public static Director mapRowToDirector(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		return new Director(id, name);
	}
}
