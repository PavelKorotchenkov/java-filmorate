package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

	private final JdbcTemplate jdbcTemplate;

	@Test
	void findAllGenre() {
		GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);

		ArrayList<Genre> genres = new ArrayList<>(genreStorage.findAllGenre());

		Assertions.assertEquals("Комедия", genres.get(0).getName());
		Assertions.assertEquals("Драма", genres.get(1).getName());
		Assertions.assertEquals("Мультфильм", genres.get(2).getName());
		Assertions.assertEquals("Триллер", genres.get(3).getName());
		Assertions.assertEquals("Документальный", genres.get(4).getName());
		Assertions.assertEquals("Боевик", genres.get(5).getName());
	}

	@Test
	void findGenreById() {
		GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
		Genre comedy = genreStorage.findGenreById(1L);
		Genre drama = genreStorage.findGenreById(2L);
		Genre cartoon = genreStorage.findGenreById(3L);
		Genre thriller = genreStorage.findGenreById(4L);
		Genre documentary = genreStorage.findGenreById(5L);
		Genre action = genreStorage.findGenreById(6L);
		Assertions.assertEquals("Комедия", comedy.getName());
		Assertions.assertEquals("Драма", drama.getName());
		Assertions.assertEquals("Мультфильм", cartoon.getName());
		Assertions.assertEquals("Триллер", thriller.getName());
		Assertions.assertEquals("Документальный", documentary.getName());
		Assertions.assertEquals("Боевик", action.getName());
	}

}