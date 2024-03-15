package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {
	Film findById(Long filmId);

	List<Film> findAll();

	Film save(Film film);

	Film update(Film film);

	List<Film> getWithDirector(Long directorId, String sortBy);

	List<Film> findPopularByGenreAndDate(int count, Integer genreId, Integer year);

	boolean deleteById(Long id);

	List<Film> getCommon(Long userId, Long friendId);

	List<Film> findBySearch(String query, String by);
}
