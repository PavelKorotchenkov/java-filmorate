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
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

	private final JdbcTemplate jdbcTemplate;

	@Test
	void findAllGenre() {
		GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);

		List<Genre> genres = new ArrayList<>(genreStorage.findAllGenre());

		Assertions.assertEquals(6, genres.size());
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
		Assertions.assertEquals(1, comedy.getId());
		Assertions.assertEquals(2, drama.getId());
		Assertions.assertEquals(3, cartoon.getId());
		Assertions.assertEquals(4, thriller.getId());
		Assertions.assertEquals(5, documentary.getId());
		Assertions.assertEquals(6, action.getId());
	}

}