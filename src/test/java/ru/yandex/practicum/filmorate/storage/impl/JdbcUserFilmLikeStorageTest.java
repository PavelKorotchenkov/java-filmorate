package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserFilmLikeStorageTest {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Test
	void addLike() {
		JdbcUserFilmLikeStorage jdbcUserFilmLikeStorage = new JdbcUserFilmLikeStorage(namedParameterJdbcTemplate);
		JdbcUserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FilmStorage filmStorage = new JdbcFilmStorage(namedParameterJdbcTemplate);

		Film newFilm = new Film(1L, "Harry Potter and Learning Java",
				"Harry's doing a hell of a job there",
				LocalDate.of(1990, 1, 1), 120000, new Mpa(2L, "PG"));

		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		userStorage.save(newUser);
		filmStorage.save(newFilm);

		long uid1 = newUser.getId();
		long fid1 = newFilm.getId();

		jdbcUserFilmLikeStorage.addLike(fid1, uid1);

		List<Film> popular = new ArrayList<>(filmStorage.findPopularByGenreAndDate(1, null, null));
		Assertions.assertEquals(fid1, popular.get(0).getId());
	}

	@Test
	void deleteLike() {
		JdbcUserFilmLikeStorage jdbcUserFilmLikeStorage = new JdbcUserFilmLikeStorage(namedParameterJdbcTemplate);
		JdbcUserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FilmStorage filmStorage = new JdbcFilmStorage(namedParameterJdbcTemplate);

		Film newFilm = new Film(1L, "Harry Potter and Learning Java",
				"Harry's doing a hell of a job there",
				LocalDate.of(1990, 1, 1), 120000, new Mpa(2L, "PG"));

		Film newFilm2 = new Film(2L, "Harry Potter and Trying to Find a Java Senior Developer Job",
				"Harry's gonna cry",
				LocalDate.of(1992, 2, 2), 500000, new Mpa(5L, "NC-17"));

		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User newUser2 = new User(2L, "user2@email.ru", "vanya1234", "Ivan Petrovsyan",
				LocalDate.of(1991, 2, 2));

		userStorage.save(newUser);
		userStorage.save(newUser2);

		filmStorage.save(newFilm);
		filmStorage.save(newFilm2);

		long uid1 = newUser.getId();
		long uid2 = newUser2.getId();

		long fid1 = newFilm.getId();
		long fid2 = newFilm2.getId();

		jdbcUserFilmLikeStorage.addLike(fid1, uid1);
		jdbcUserFilmLikeStorage.addLike(fid1, uid2);
		jdbcUserFilmLikeStorage.addLike(fid2, uid1);
		jdbcUserFilmLikeStorage.addLike(fid2, uid2);

		jdbcUserFilmLikeStorage.deleteLike(fid1, uid1);

		List<Film> popular = new ArrayList<>(filmStorage.findPopularByGenreAndDate(2, null, null));
		Assertions.assertEquals(fid2, popular.get(0).getId());
		Assertions.assertEquals(fid1, popular.get(1).getId());
	}
}