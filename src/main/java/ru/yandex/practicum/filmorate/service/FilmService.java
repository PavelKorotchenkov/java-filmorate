package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {
	private final FilmStorage filmStorage;

	@Autowired
	public FilmService(FilmStorage filmStorage) {
		this.filmStorage = filmStorage;

	}

	public Film addFilm(Film film) {
		return filmStorage.save(film);
	}

	public Collection<Film> getAllFilms() {
		return filmStorage.findAllFilms();
	}

	public Film getFilmById(Long filmId) {
		return filmStorage.findFilmById(filmId);
	}

	public Film updateFilm(Film film) {
		return filmStorage.update(film);
	}

}
