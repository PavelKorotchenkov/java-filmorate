package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
	private final FilmStorage filmStorage;
	private final UserFilmLikeStorage userFilmLikeStorage;
	private final UserStorage userStorage;

	@Autowired
	public FilmService(FilmStorage filmStorage, UserFilmLikeStorage userFilmLikeStorage, UserStorage userStorage) {
		this.filmStorage = filmStorage;
		this.userFilmLikeStorage = userFilmLikeStorage;
		this.userStorage = userStorage;
	}

	public Film add(Film film) {
		return filmStorage.save(film);
	}

	public List<Film> getAll() {
		return filmStorage.findAll();
	}

	public Film getById(Long filmId) {
		return filmStorage.findById(filmId);
	}

	public Film update(Film film) {
		return filmStorage.update(film);
	}

	public List<Film> showPopularByGenreAndDate(int count, Integer genreId, Integer year) {

		return userFilmLikeStorage.findPopularByGenreAndDate(count, genreId, year);
	}

	public List<Film> getCommon(Long userId, Long friendId) {
		User user = userStorage.findById(userId);
		User friend = userStorage.findById(friendId);
		return userFilmLikeStorage.getCommon(user.getId(), friend.getId());
	}

	public List<Film> getWithDirector(Long directorId, String sortBy) {
		return filmStorage.getWithDirector(directorId, sortBy);
	}

	public void delete(Long id) {
		filmStorage.deleteById(id);
	}

	public List<Film> getBySearch(String query, String by) {
		return filmStorage.findBySearch(query, by);
	}
}
