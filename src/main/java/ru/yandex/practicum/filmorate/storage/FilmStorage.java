package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
@Component
public interface FilmStorage {
	Film findFilm(Long filmId);
	Collection<Film> getAllFilms();
	Film add(Film film);
	Film delete(Long filmId);
	Film update(Film film);
}
