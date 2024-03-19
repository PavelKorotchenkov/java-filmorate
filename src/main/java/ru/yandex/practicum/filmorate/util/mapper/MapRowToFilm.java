package ru.yandex.practicum.filmorate.util.mapper;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

public class MapRowToFilm {
	public static Film map(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		String description = row.getString("description");
		LocalDate releaseDate = row.getDate("releaseDate").toLocalDate();
		long duration = row.getLong("duration");
		long mpaId = row.getInt("mpa_id");
		String mpaName = row.getString("mpa_name");
		Mpa mpa = new Mpa(mpaId, mpaName);

		Film film = new Film(id, name, description, releaseDate, duration, mpa);

		try {
			long genreId = row.getLong("genre_id");
			String genreName = row.getString("genre_name");
			if (genreId != 0) {
				Genre genre = new Genre(genreId, genreName);
				Set<Genre> genres = new LinkedHashSet<>();
				genres.add(genre);
				film.setGenres(genres);
			}
		} catch (SQLException e) {
			System.out.println();
		}

		try {
			long directorId = row.getLong("director_id");
			String directorName = row.getString("director_name");
			if (directorId != 0) {
				Director director = new Director(directorId, directorName);
				Set<Director> directors = new LinkedHashSet<>();
				directors.add(director);
				film.setDirectors(directors);
			}
		} catch (SQLException e) {
			System.out.println();
		}
		return film;
	}
}
