package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {
    Film findFilmById(Long filmId);

    List<Film> findAllFilms();

    Film save(Film film);

    Film update(Film film);

    List<Film> getFilmsWithDirector(Long directorId, String sortBy);

    boolean deleteById(Long id);
}
