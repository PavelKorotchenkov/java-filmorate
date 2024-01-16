package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;

	public Film addFilm(Film film) {
		return filmStorage.add(film);
	}

	public Collection<Film> getAllFilms() {
		return filmStorage.getAllFilms();
	}

	public Film getFilmById(Long filmId) {
		return filmStorage.findFilm(filmId);
	}

	public Film updateFilm(Film film) {
		return filmStorage.update(film);
	}

	public void addLike(Long filmId, Long userId) {
		Film film = filmStorage.findFilm(filmId);
		User user = userStorage.findUser(userId);
		film.addToLikedUsers(userId);
		user.addToLikedFilms(filmId);
	}

	public void deleteLike(Long filmId, Long userId) {
		Film film = filmStorage.findFilm(filmId);
		User user = userStorage.findUser(userId);
		film.deleteFromLikedUsers(userId);
		user.deleteFromLikedFilms(filmId);
	}

	public List<Film> showMostLikedFilms(int topFilmsSize) {
		return filmStorage.getAllFilms().stream()
				.sorted(this::compareByLikes)
				.limit(topFilmsSize)
				.collect(Collectors.toList());
	}

	private int compareByLikes(Film f0, Film f1) {
		Integer f0Size = f0.getLikedUsers().size();
		Integer f1Size = f1.getLikedUsers().size();
		int result = -1 * f0Size.compareTo(f1Size);

		return result;
	}
}
