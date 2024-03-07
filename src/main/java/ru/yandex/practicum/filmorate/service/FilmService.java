package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserFilmLikeStorage userFilmLikeStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserFilmLikeStorage userFilmLikeStorage) {
        this.filmStorage = filmStorage;

        this.userFilmLikeStorage = userFilmLikeStorage;
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
        return userFilmLikeStorage.getAllCommonFilms(userId, friendId);
    }

    public List<Film> getFilmsWithDirector(Long directorId, String sortBy) {
        return filmStorage.getFilmsWithDirector(directorId, sortBy);
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteById(id);
    }
}
