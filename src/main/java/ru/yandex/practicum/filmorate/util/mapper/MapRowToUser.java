package ru.yandex.practicum.filmorate.util.mapper;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MapRowToUser {
	public static User map(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String email = row.getString("email");
		String login = row.getString("login");
		String name = row.getString("name");
		LocalDate birthday = row.getDate("birthday").toLocalDate();
		return new User(id, email, login, name, birthday);
	}
}
