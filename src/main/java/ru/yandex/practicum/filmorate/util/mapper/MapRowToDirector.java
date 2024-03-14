package ru.yandex.practicum.filmorate.util.mapper;

import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowToDirector {
	public static Director map(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		return new Director(id, name);
	}
}
