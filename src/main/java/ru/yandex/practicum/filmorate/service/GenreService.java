package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {
	private final GenreStorage genreStorage;

	@Autowired
	public GenreService(GenreStorage genreStorage) {
		this.genreStorage = genreStorage;
	}

	public Collection<Genre> getGenres() {
		return genreStorage.findAllGenre();
	}

	public Genre getGenreById(Long id) {
		return genreStorage.findGenreById(id);
	}
}
