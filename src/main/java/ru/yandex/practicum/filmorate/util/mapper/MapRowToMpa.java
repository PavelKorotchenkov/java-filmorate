package ru.yandex.practicum.filmorate.util.mapper;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowToMpa {
	public static Mpa map(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String name = row.getString("name");
		return new Mpa(id, name);
	}
}
