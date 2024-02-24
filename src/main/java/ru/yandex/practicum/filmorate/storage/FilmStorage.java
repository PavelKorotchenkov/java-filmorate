package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public interface FilmStorage {
	Film findFilmById(Long filmId);

	Collection<Film> findAllFilms();

	Film save(Film film);

	Film update(Film film);
}
