package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserFilmLikeDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserFilmLikeDbStorageTest {

	private final JdbcTemplate jdbcTemplate;

	@Test
	void findPopular() {
		UserFilmLikeDbStorage userFilmLikeDbStorage = new UserFilmLikeDbStorage(jdbcTemplate);
		UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
		FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

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

		userFilmLikeDbStorage.addLike(fid1, uid1);
		userFilmLikeDbStorage.addLike(fid1, uid2);
		userFilmLikeDbStorage.addLike(fid2, uid1);

		List<Film> popular = new ArrayList<>(userFilmLikeDbStorage.findPopular(2));
		Assertions.assertEquals(fid1, popular.get(0).getId());
		Assertions.assertEquals(fid2, popular.get(1).getId());
	}

	@Test
	void addLike() {
		UserFilmLikeDbStorage userFilmLikeDbStorage = new UserFilmLikeDbStorage(jdbcTemplate);
		UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
		FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

		Film newFilm = new Film(1L, "Harry Potter and Learning Java",
				"Harry's doing a hell of a job there",
				LocalDate.of(1990, 1, 1), 120000, new Mpa(2L, "PG"));

		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		userStorage.save(newUser);
		filmStorage.save(newFilm);

		long uid1 = newUser.getId();
		long fid1 = newFilm.getId();

		userFilmLikeDbStorage.addLike(fid1, uid1);

		List<Film> popular = new ArrayList<>(userFilmLikeDbStorage.findPopular(1));
		Assertions.assertEquals(fid1, popular.get(0).getId());
	}

	@Test
	void deleteLike() {
		UserFilmLikeDbStorage userFilmLikeDbStorage = new UserFilmLikeDbStorage(jdbcTemplate);
		UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
		FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);

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

		userFilmLikeDbStorage.addLike(fid1, uid1);
		userFilmLikeDbStorage.addLike(fid1, uid2);
		userFilmLikeDbStorage.addLike(fid2, uid1);
		userFilmLikeDbStorage.addLike(fid2, uid2);

		userFilmLikeDbStorage.deleteLike(fid1, uid1);

		List<Film> popular = new ArrayList<>(userFilmLikeDbStorage.findPopular(2));
		Assertions.assertEquals(fid2, popular.get(0).getId());
		Assertions.assertEquals(fid1, popular.get(1).getId());
	}
}