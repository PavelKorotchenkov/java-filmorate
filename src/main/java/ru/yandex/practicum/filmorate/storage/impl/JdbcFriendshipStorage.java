package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToUser;

import java.util.List;

@Repository
public class JdbcFriendshipStorage implements FriendshipStorage {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public JdbcFriendshipStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<User> findAll(Long id) {
		String sql = "SELECT id, email, name, login, birthday " +
				"FROM users " +
				"WHERE id IN (" +
				"SELECT friend_id FROM friendship " +
				"WHERE user_id = :userId)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", id);

		return namedParameterJdbcTemplate.query(sql, params, MapRowToUser::map);
	}

	@Override
	public List<User> findMutual(Long userId, Long friendId) {
		String sql = "SELECT u.id, u.email, u.name, u.login, u.birthday " +
				"FROM users u " +
				"WHERE u.id IN (" +
				"SELECT friend_id FROM friendship " +
				"WHERE user_id = :userId AND friend_id IN " +
				"(SELECT friend_id FROM friendship WHERE user_id = :friendId))";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);
		params.addValue("friendId", friendId);

		return namedParameterJdbcTemplate.query(sql, params, MapRowToUser::map);
	}

	@Override
	public boolean add(Long userId, Long friendId) {
		String checkSql = "SELECT COUNT(*) FROM friendship WHERE user_id = :friendId AND friend_id = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);
		params.addValue("friendId", friendId);

		int count = namedParameterJdbcTemplate.queryForObject(checkSql, params, Integer.class);

		if (count == 0) {
			String insertSql = "INSERT INTO friendship (user_id, friend_id, friendship_status) " +
					"VALUES (:userId, :friendId, :status)";
			MapSqlParameterSource insertParams = new MapSqlParameterSource();
			insertParams.addValue("userId", userId);
			insertParams.addValue("friendId", friendId);
			insertParams.addValue("status", false);

			namedParameterJdbcTemplate.update(insertSql, insertParams);
			return false;
		} else {
			String updateSql = "UPDATE friendship " +
					"SET friendship_status = :status WHERE user_id = :userId AND friend_id = :friendId";
			MapSqlParameterSource updateParams = new MapSqlParameterSource();
			updateParams.addValue("status", true);
			updateParams.addValue("userId", userId);
			updateParams.addValue("friendId", friendId);
			namedParameterJdbcTemplate.update(updateSql, updateParams);

			MapSqlParameterSource updateBack = new MapSqlParameterSource();
			updateBack.addValue("status", true);
			updateBack.addValue("userId", friendId);
			updateBack.addValue("friendId", userId);
			namedParameterJdbcTemplate.update(updateSql, updateBack);
			return true;
		}
	}

	@Override
	public void delete(Long userId, Long friendId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);
		params.addValue("friendId", friendId);

		namedParameterJdbcTemplate.update(
				"DELETE FROM friendship " +
						"WHERE user_id = :userId " +
						"AND friend_id = :friendId",
				params
		);

		namedParameterJdbcTemplate.update(
				"UPDATE friendship SET friendship_status = :status " +
						"WHERE user_id = :friendId " +
						"AND friend_id = :userId",
				new MapSqlParameterSource("status", false)
						.addValue("userId", userId)
						.addValue("friendId", friendId)
		);
	}
}
