package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.util.List;

@Slf4j
@Repository
public class FriendshipDbStorage implements FriendshipStorage {

	private final JdbcTemplate jdbcTemplate;

	public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean addFriend(Long userId, Long friendId) {
		List<Friendship> result = jdbcTemplate.query(
				"SELECT user_id, friend_id, friendship_status " +
						"FROM friendship " +
						"WHERE user_id = ? " +
						"AND friend_id = ?",
				RowMapper::mapRowToFriendship,
				friendId,
				userId
		);

		if (result.isEmpty()) {
			jdbcTemplate.update(
					"INSERT INTO friendship (user_id, friend_id, friendship_status) " +
							"VALUES (?, ?, ?)",
					userId,
					friendId,
					false);
			log.info("Друг добавлен");
			return false;
		} else {
			jdbcTemplate.update(
					"INSERT INTO friendship (user_id, friend_id, friendship_status) " +
							"VALUES (?, ?, ?)",
					userId,
					friendId,
					true);

			jdbcTemplate.update(
					"UPDATE friendship SET friendship_status = ?" +
							"WHERE user_id = ?" +
							"AND friend_id = ?",
					true,
					friendId,
					userId);
			log.info("Друг добавлен");
			return true;
		}
	}

	@Override
	public List<User> findAllFriends(Long id) {
		log.info("Запрос всех друзей пользователя");
		return jdbcTemplate.query(
				"SELECT id, email, name, login, birthday " +
						"FROM users " +
						"WHERE id IN (" +
						"SELECT friend_id FROM friendship " +
						"WHERE user_id = ?)",
				RowMapper::mapRowToUser,
				id);
	}

	@Override
	public List<User> findAllMutualFriends(Long userId, Long friendId) {
		log.info("Запрос всех общих друзей с другом");

		return jdbcTemplate.query(
				"SELECT u.id, u.email, u.name, u.login, u.birthday " +
						"FROM users u " +
						"WHERE u.id in (" +
						"SELECT friend_id FROM friendship " +
						"WHERE user_id = ? AND friend_id IN" +
						"(SELECT friend_id " +
						"FROM friendship " +
						"WHERE user_id = ?))",
				RowMapper::mapRowToUser,
				userId,
				friendId);
	}

	@Override
	public void deleteFriend(Long userId, Long friendId) {
		jdbcTemplate.update(
				"DELETE FROM friendship " +
						"WHERE user_id = ? " +
						"AND friend_id = ? ",
				userId,
				friendId
		);

		jdbcTemplate.update(
				"UPDATE friendship SET friendship_status = ?" +
						"WHERE user_id = ?" +
						"AND friend_id = ?",
				false,
				friendId,
				userId);
		log.info("Друг удален");
	}
}
