package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserStorageTest {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Test
	void findUserById() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));
		JdbcUserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);

		User savedUser = userStorage.findById(newUser.getId());

		assertThat(savedUser)
				.isNotNull()
				.usingRecursiveComparison()
				.isEqualTo(newUser);
	}

	@Test
	void findAllUsers() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User newUser2 = new User(2L, "user2@email.ru", "vanya1234", "Ivan Petrovsyan",
				LocalDate.of(1991, 2, 2));

		JdbcUserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(newUser2);

		List<User> users = userStorage.findAll();

		assertEquals(2, users.size());
		Assertions.assertTrue(users.contains(newUser));
		Assertions.assertTrue(users.contains(newUser2));
	}

	@Test
	void update() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User updatedUser = new User(1L, "userilla@email.ru", "userling", "Uz Er",
				LocalDate.of(2010, 3, 12));

		JdbcUserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		long id = newUser.getId();
		updatedUser.setId(id);
		userStorage.update(updatedUser);

		User user2 = userStorage.findById(id);

		assertThat(user2)
				.isNotNull()
				.usingRecursiveComparison()
				.isEqualTo(updatedUser);
	}
}