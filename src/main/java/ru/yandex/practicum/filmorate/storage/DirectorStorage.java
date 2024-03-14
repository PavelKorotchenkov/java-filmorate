package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    List<Director> getAll();

    Optional<Director> getById(Long directorId);

    Director create(Director director);

    Optional<Director> update(Director director);

    void delete(Long directorId);
}
