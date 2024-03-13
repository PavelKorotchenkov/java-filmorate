package ru.yandex.practicum.filmorate.storage.mem;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long filmID = 0L;

    @Override
    public Film findById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("No film in database with id " + filmId);
        }
        return films.get(filmId);
    }

    @Override
    public List<Film> findAll() {
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

    @Override
    public List<Film> getWithDirector(Long directorId, String sortBy) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public List<Film> findBySearch(String query, String by) {
        return null;
    }

    public Film delete(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("No film in database with id " + filmId);
        }
        return films.remove(filmId);
    }
}
