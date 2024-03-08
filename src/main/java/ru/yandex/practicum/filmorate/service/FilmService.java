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

    public Film addFilm(Film film) {
        return filmStorage.save(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> showPopularByGenreAndDate(int count, Integer genreId, Integer year) {

        return userFilmLikeStorage.findPopularByGenreAndDate(count, genreId, year);
    }

    public List<Film> getFilmCommon(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        return userFilmLikeStorage.getAllCommonFilms(user.getId(), friend.getId());
    }

    public List<Film> getFilmsWithDirector(Long directorId, String sortBy) {
        return filmStorage.getFilmsWithDirector(directorId, sortBy);
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteById(id);
    }
}
