package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToUser;

import java.util.List;

@Repository
@Primary
public class JdbcUserStorage implements UserStorage {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcUserStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public User findById(Long id) {
		String sql = "SELECT id, email, name, login, birthday FROM users WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		List<User> result = namedParameterJdbcTemplate.query(sql, params, MapRowToUser::map);
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public List<User> findAll() {
		String sql = "SELECT id, email, name, login, birthday FROM users";
		return namedParameterJdbcTemplate.query(sql, MapRowToUser::map);
	}

	@Override
	public User save(User user) {
		String sql = "INSERT INTO users (email, name, login, birthday) VALUES (:email, :name, :login, :birthday)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("email", user.getEmail());
		params.addValue("name", user.getName());
		params.addValue("login", user.getLogin());
		params.addValue("birthday", user.getBirthday());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, params, keyHolder);
		long generatedId = keyHolder.getKey().longValue();
		user.setId(generatedId);
		return user;
	}

	@Override
	public User update(User user) {
		String sql = "UPDATE users SET email = :email, name = :name, login = :login, birthday = :birthday WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("email", user.getEmail());
		params.addValue("name", user.getName());
		params.addValue("login", user.getLogin());
		params.addValue("birthday", user.getBirthday());
		params.addValue("id", user.getId());

		namedParameterJdbcTemplate.update(sql, params);
		return user;
	}

	@Override
	public boolean deleteById(Long id) {
		String sql = "DELETE FROM users WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		return namedParameterJdbcTemplate.update(sql, params) > 0;
	}
}
