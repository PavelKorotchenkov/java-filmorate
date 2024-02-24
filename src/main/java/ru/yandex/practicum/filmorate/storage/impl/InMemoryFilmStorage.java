package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
	private final Map<Long, Film> films;
	private Long filmID = 0L;

	@Autowired
	public InMemoryFilmStorage(Map<Long, Film> map) {
		this.films = map;
	}

	@Override
	public Film findFilmById(Long filmId) {
		if (!films.containsKey(filmId)) {
			throw new NotFoundException("No film in database with id " + filmId);
		}
		return films.get(filmId);
	}

	@Override
	public Collection<Film> findAllFilms() {
		return List.copyOf(films.values());
	}

	@Override
	public Film save(Film film) {
		film.setId(++filmID);
		return films.put(film.getId(), film);
	}

	@Override
	public Film update(Film film) {
		if (!films.containsKey(film.getId())) {
			throw new NotFoundException("No film in database with id " + film.getId());
		}
		return films.put(film.getId(), film);
	}

	public Film delete(Long filmId) {
		if (!films.containsKey(filmId)) {
			throw new NotFoundException("No film in database with id " + filmId);
		}
		return films.remove(filmId);
	}
}
