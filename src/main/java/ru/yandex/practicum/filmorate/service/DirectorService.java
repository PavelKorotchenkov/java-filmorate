package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {
	private final DirectorStorage directorStorage;

	public DirectorService(DirectorStorage directorStorage) {
		this.directorStorage = directorStorage;
	}

	public List<Director> getAll() {
		return directorStorage.getAll();
	}

	public Director getById(Long directorId) {
		Director director = directorStorage.getById(directorId);
		if (director == null) {
			throw new NotFoundException("Режиссер с id " + directorId + " не найден");
		}
		return director;
	}

	public Director create(Director director) {
		return directorStorage.create(director);
	}

	public Director update(Director director) {
		Director dir = directorStorage.getById(director.getId());
		if (dir == null) {
			throw new NotFoundException("Режиссер с id " + director.getId() + " не найден");
		}
		return directorStorage.update(director);
	}

	public void delete(Long directorId) {
		directorStorage.delete(directorId);
	}
}
