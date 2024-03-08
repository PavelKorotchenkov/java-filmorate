package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getAll();

    Director getById(Long directorId);

    Director create(Director director);

    Director update(Director director);

    void delete(Long directorId);
}
