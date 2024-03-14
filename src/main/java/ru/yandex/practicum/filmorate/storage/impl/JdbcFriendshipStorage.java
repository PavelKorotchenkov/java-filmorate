package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcFriendshipStorage implements FriendshipStorage {

	private final JdbcTemplate jdbcTemplate;

	public JdbcFriendshipStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean add(Long userId, Long friendId) {
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
			return true;
		}
	}

	@Override
	public List<User> findAll(Long id) {
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
	public List<User> findMutual(Long userId, Long friendId) {
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
	public void delete(Long userId, Long friendId) {
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
	}
}
