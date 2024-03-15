package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class LikeService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	private final UserFilmLikeStorage userFilmLikeStorage;

	@Autowired
	public LikeService(FilmStorage filmStorage, UserStorage userStorage, UserFilmLikeStorage userFilmLikeStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
		this.userFilmLikeStorage = userFilmLikeStorage;
	}

	public void add(Long filmId, Long userId) {
		Film film = filmStorage.findById(filmId);
		if (film == null) {
			throw new NotFoundException("Фильм с id " + filmId + " не найден");
		}
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		userFilmLikeStorage.addLike(film.getId(), user.getId());
	}

	public void delete(Long filmId, Long userId) {
		Film film = filmStorage.findById(filmId);
		if (film == null) {
			throw new NotFoundException("Фильм с id " + filmId + " не найден");
		}
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}

		userFilmLikeStorage.deleteLike(film.getId(), user.getId());
	}
}
