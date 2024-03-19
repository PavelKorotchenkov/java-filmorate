package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFriendshipStorageTest {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Test
	void addOneSidedFriendship() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User friend = new User(2L, "bf@email.ru", "chipichipi", "chapachapa",
				LocalDate.of(1991, 2, 2));

		UserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FriendshipStorage friendshipStorage = new JdbcFriendshipStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(friend);

		boolean isConfirmed = friendshipStorage.add(newUser.getId(), friend.getId());

		Assertions.assertFalse(isConfirmed);
	}

	@Test
	void addTwoSidedFriendship() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User friend = new User(2L, "bf@email.ru", "chipichipi", "chapachapa",
				LocalDate.of(1991, 2, 2));

		UserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FriendshipStorage friendshipStorage = new JdbcFriendshipStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(friend);

		friendshipStorage.add(newUser.getId(), friend.getId());
		friendshipStorage.add(friend.getId(), newUser.getId());
		boolean isConfirmed = friendshipStorage.add(friend.getId(), newUser.getId());

		Assertions.assertTrue(isConfirmed);
	}

	@Test
	void findAllFriends() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User friend = new User(2L, "bf@email.ru", "chipichipi", "chapachapa",
				LocalDate.of(1991, 2, 2));

		User friend2 = new User(3L, "bff@email.ru", "dubidubi", "dabadaba",
				LocalDate.of(1992, 3, 22));

		UserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FriendshipStorage friendshipStorage = new JdbcFriendshipStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(friend);
		userStorage.save(friend2);

		friendshipStorage.add(newUser.getId(), friend.getId());
		friendshipStorage.add(newUser.getId(), friend2.getId());
		List<User> friends = friendshipStorage.findAll(newUser.getId());

		Assertions.assertTrue(friends.contains(friend));
		Assertions.assertTrue(friends.contains(friend2));
	}

	@Test
	void findAllMutualFriends() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User friend = new User(2L, "bf@email.ru", "chipichipi", "chapachapa",
				LocalDate.of(1991, 2, 2));

		User friend2 = new User(3L, "bff@email.ru", "dubidubi", "dabadaba",
				LocalDate.of(1992, 3, 22));

		UserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FriendshipStorage friendshipStorage = new JdbcFriendshipStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(friend);
		userStorage.save(friend2);

		friendshipStorage.add(newUser.getId(), friend2.getId());
		friendshipStorage.add(friend.getId(), friend2.getId());
		List<User> friends = friendshipStorage.findMutual(newUser.getId(), friend.getId());

		Assertions.assertTrue(friends.contains(friend2));
	}

	@Test
	void deleteFriend() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User friend = new User(2L, "bf@email.ru", "chipichipi", "chapachapa",
				LocalDate.of(1991, 2, 2));

		User friend2 = new User(3L, "bff@email.ru", "dubidubi", "dabadaba",
				LocalDate.of(1992, 3, 22));

		UserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FriendshipStorage friendshipStorage = new JdbcFriendshipStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(friend);
		userStorage.save(friend2);

		friendshipStorage.add(newUser.getId(), friend.getId());
		friendshipStorage.add(newUser.getId(), friend2.getId());

		friendshipStorage.delete(newUser.getId(), friend2.getId());
		List<User> friends = friendshipStorage.findAll(newUser.getId());

		Assertions.assertTrue(friends.contains(friend));
		Assertions.assertFalse(friends.contains(friend2));
	}

	@Test
	void afterDeleteFriendOneSidedFriendship() {
		User newUser = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
				LocalDate.of(1990, 1, 1));

		User friend = new User(2L, "bf@email.ru", "chipichipi", "chapachapa",
				LocalDate.of(1991, 2, 2));

		UserStorage userStorage = new JdbcUserStorage(namedParameterJdbcTemplate);
		FriendshipStorage friendshipStorage = new JdbcFriendshipStorage(namedParameterJdbcTemplate);
		userStorage.save(newUser);
		userStorage.save(friend);

		friendshipStorage.add(newUser.getId(), friend.getId());
		friendshipStorage.add(friend.getId(), newUser.getId());
		friendshipStorage.delete(newUser.getId(), friend.getId());

		boolean isConfirmed = friendshipStorage.add(friend.getId(), newUser.getId());

		Assertions.assertFalse(isConfirmed);
	}
}