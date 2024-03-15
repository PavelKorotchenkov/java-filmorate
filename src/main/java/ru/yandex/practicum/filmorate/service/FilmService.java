package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;
	private final DirectorStorage directorStorage;
	private final GenreStorage genreStorage;

	@Autowired
	public FilmService(FilmStorage filmStorage, UserStorage userStorage, DirectorStorage directorStorage, GenreStorage genreStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
		this.directorStorage = directorStorage;
		this.genreStorage = genreStorage;
	}

	public Film add(Film film) {
		return filmStorage.save(film);
	}

	public List<Film> getAll() {
		return filmStorage.findAll();
	}

	public Film getById(Long filmId) {
		Film film = filmStorage.findById(filmId);
		System.out.println(filmId);
		if (film == null) {
			throw new NotFoundException("Фильм с id " + filmId + " не найден");
		}
		return film;
	}

	public Film update(Film film) {
		Film filmById = filmStorage.findById(film.getId());
		if (filmById == null) {
			throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
		}
		return filmStorage.update(film);
	}

	public List<Film> showPopularByGenreAndDate(int count, Long genreId, Integer year) {
		if (genreId != null) {
			Genre genre = genreStorage.findById(genreId);
			if (genre == null) {
				throw new NotFoundException("Жанр с id " + genreId + " не найден");
			}
		}
		return filmStorage.findPopularByGenreAndDate(count, genreId, year);
	}

	public List<Film> getCommon(Long userId, Long friendId) {
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		User friend = userStorage.findById(friendId);
		if (friend == null) {
			throw new NotFoundException("Пользователь с id " + friendId + " не найден");
		}
		return filmStorage.getCommon(user.getId(), friend.getId());
	}

	public List<Film> getWithDirector(Long directorId, String sortBy) {
		Director director = directorStorage.getById(directorId);
		if (director == null) {
			throw new NotFoundException("Режиссёр с id " + directorId + "не найден");
		}
		return filmStorage.getWithDirector(directorId, sortBy);
	}

	public void delete(Long id) {
		filmStorage.deleteById(id);
	}

	public List<Film> getBySearch(String query, String by) {
		return filmStorage.findBySearch(query, by);
	}
}
