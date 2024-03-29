package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
	private final GenreStorage genreStorage;

	@Autowired
	public GenreService(GenreStorage genreStorage) {
		this.genreStorage = genreStorage;
	}

	public List<Genre> getAll() {
		return genreStorage.findAll();
	}

	public Genre getById(Long id) {
		Genre byId = genreStorage.findById(id);
		if (byId == null) {
			throw new NotFoundException("Жанр с id " + id + " не найден");
		}
		return byId;
	}
}
