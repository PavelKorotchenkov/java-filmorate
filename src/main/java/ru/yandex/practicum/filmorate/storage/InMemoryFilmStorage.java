package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
	private final Map<Long, Film> films = new HashMap<>();
	private Long filmID = 0L;

	@Override
	public Film findFilm(Long filmId) {
		if (!films.containsKey(filmId)) {
			throw new NotFoundException("No film in database with id " + filmId);
		}
		return films.get(filmId);
	}

	@Override
	public Collection<Film> getAllFilms() {
		return List.copyOf(films.values());
	}

	@Override
	public Film add(Film film) {
		film.setId(++filmID);
		return films.put(film.getId(), film);
	}

	@Override
	public Film delete(Long filmId) {
		if (!films.containsKey(filmId)) {
			throw new NotFoundException("No film in database with id " + filmId);
		}
		return films.remove(filmId);
	}

	@Override
	public Film update(Film film) {
		if (!films.containsKey(film.getId())) {
			throw new NotFoundException("No film in database with id " + film.getId());
		}
		return films.put(film.getId(), film);
	}
}
