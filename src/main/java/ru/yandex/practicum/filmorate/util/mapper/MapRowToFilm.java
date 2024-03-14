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

		Set<Genre> genreSet = new LinkedHashSet<>();
		String genreRowData = row.getString("genre");

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

		Set<Director> directorSet = new LinkedHashSet<>();
		String directorRowData = "";
		try {
			directorRowData = row.getString("director");
		} catch (SQLException e) {

		}

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
		Film film = new Film(id, name, description, releaseDate, duration, mpa);
		film.setGenres(genreSet);
		film.setDirectors(directorSet);
		return film;
	}
}
