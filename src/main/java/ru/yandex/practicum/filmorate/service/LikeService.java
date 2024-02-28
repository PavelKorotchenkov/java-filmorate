package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	public void addLike(Long filmId, Long userId) {
		Film film = filmStorage.findFilmById(filmId); //проверяем, что фильм есть в базе
		User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе

		userFilmLikeStorage.addLike(film.getId(), user.getId());
	}

	public void deleteLike(Long filmId, Long userId) {
		Film film = filmStorage.findFilmById(filmId); //проверяем, что фильм есть в базе
		User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе

		userFilmLikeStorage.deleteLike(film.getId(), user.getId());
	}
}
