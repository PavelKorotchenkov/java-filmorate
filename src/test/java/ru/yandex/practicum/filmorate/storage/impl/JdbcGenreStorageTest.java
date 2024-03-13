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
class JdbcGenreStorageTest {

	private final JdbcTemplate jdbcTemplate;

	@Test
	void findAllGenre() {
		GenreStorage genreStorage = new JdbcGenreStorage(jdbcTemplate);

		List<Genre> genres = new ArrayList<>(genreStorage.findAll());

		Assertions.assertEquals(6, genres.size());
	}

	@Test
	void findGenreById() {
		GenreStorage genreStorage = new JdbcGenreStorage(jdbcTemplate);
		Genre comedy = genreStorage.findById(1L);
		Genre drama = genreStorage.findById(2L);
		Genre cartoon = genreStorage.findById(3L);
		Genre thriller = genreStorage.findById(4L);
		Genre documentary = genreStorage.findById(5L);
		Genre action = genreStorage.findById(6L);
		Assertions.assertEquals(1, comedy.getId());
		Assertions.assertEquals(2, drama.getId());
		Assertions.assertEquals(3, cartoon.getId());
		Assertions.assertEquals(4, thriller.getId());
		Assertions.assertEquals(5, documentary.getId());
		Assertions.assertEquals(6, action.getId());
	}

}