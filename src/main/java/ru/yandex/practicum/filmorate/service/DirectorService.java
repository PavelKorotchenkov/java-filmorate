package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getById(Long directorId) {
        Optional<Director> result = directorStorage.getById(directorId);
        if (result.isPresent()) {
            log.info("Получен режиссёр: {}", result);
            return result.get();
        } else {
            throw new NotFoundException("Режиссёр с id " + directorId + " не найден");
        }
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        Optional<Director> result = directorStorage.update(director);
        if (result.isPresent()) {
            log.info("Изменен режиссёр: {}", result);
            return result.get();
        } else {
            throw new NotFoundException("Режиссёр с id " + director.getId() + " не найден");
        }
    }

    public void delete(Long directorId) {
        directorStorage.delete(directorId);
    }
}
