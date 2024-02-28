package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
@Component
@Primary
public class UserDbStorage implements UserStorage {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User findUserById(Long id) {
		List<User> result = jdbcTemplate.query(
				"SELECT id, email, name, login, birthday FROM users WHERE id = ?",
				RowMapper::mapRowToUser,
				id
		);
		if (result.isEmpty()) {
			log.info("Пользователь с идентификатором {} не найден.", id);
			throw new NotFoundException("Пользователь с id " + id + " не найден.");
		}
		log.info("Найден пользователь с id: {}", id);

		return result.get(0);
	}

	@Override
	public List<User> findAllUsers() {
		log.info("Запрос всех пользователей");
		return jdbcTemplate.query(
				"SELECT id, email, name, login, birthday FROM users",
				RowMapper::mapRowToUser);
	}

	@Override
	public User save(User user) {
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"INSERT INTO users (email, name, login, birthday) VALUES (?, ?, ?, ?)",
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
		pscf.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc =
				pscf.newPreparedStatementCreator(
						Arrays.asList(
								user.getEmail(),
								user.getName(),
								user.getLogin(),
								user.getBirthday()));
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		long orderId = keyHolder.getKey().longValue();
		user.setId(orderId);
		log.info("Создан новый пользователь: " + user);
		return user;
	}

	@Override
	public User update(User user) {
		Long id = user.getId();
		findUserById(id); //проверяем, что пользователь есть в базе

		jdbcTemplate.update("UPDATE users SET email = ?, name = ?, login = ?, birthday = ? WHERE id = ?",
				user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), id);

		log.info("Обновлен пользователь: " + user);

		return user;
	}
}
