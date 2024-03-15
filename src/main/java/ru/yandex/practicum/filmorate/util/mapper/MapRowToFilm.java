package ru.yandex.practicum.filmorate.util.mapper;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
		return film;
	}
}
