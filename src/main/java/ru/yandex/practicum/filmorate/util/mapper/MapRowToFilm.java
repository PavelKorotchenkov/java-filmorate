package ru.yandex.practicum.filmorate.util.mapper;
//
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.Genre;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Slf4j//TODO надо убрать
public class MapRowToFilm {
	public static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
		log.info("wpadlawopdkawpodka");
		return Film.builder()
				.id(resultSet.getLong("id"))
				.name(resultSet.getString("name"))
				.description(resultSet.getString("description"))
				.releaseDate(resultSet.getDate("releaseDate").toLocalDate())
				.duration(resultSet.getInt("duration"))
				.mpa(mapRowToMpa(resultSet))
				.genres(mapRowGenre(resultSet))
				.build();

	}

	private static Mpa mapRowToMpa(ResultSet resultSet) throws SQLException {
		return Mpa.builder()
				.id(resultSet.getLong("mpa_id"))
				.name(resultSet.getString("mpa_name"))
				.build();
	}
	private static Director mapRowToDirector(ResultSet resultSet) throws SQLException {
		return Director.builder()
				.id(resultSet.getLong("director_id"))
				.name(resultSet.getString("director_name"))
				.build();
	}

	private static Set<Genre> mapRowGenre(ResultSet resultSet) throws SQLException {
		Set<Genre> genres = new HashSet<>();
		String genreIds = resultSet.getString("genre_id");
		String genreNames = resultSet.getString("genre_name");
		if (genreIds != null) {
			String[] ids = genreIds.split(",");
			String[] names = genreNames.split(",");
			for (int i = 0; i < ids.length; i++) {
				genres.add(Genre.builder()
						.id(Long.parseLong(ids[i]))
						.name(names[i])
						.build());
			}
		}
		return genres;
	}
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
