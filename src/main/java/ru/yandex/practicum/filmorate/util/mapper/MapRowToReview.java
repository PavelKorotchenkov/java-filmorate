package ru.yandex.practicum.filmorate.util.mapper;

import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowToReview {
	public static Review map(ResultSet row, int rowNum) throws SQLException {
		Long id = row.getLong("id");
		String content = row.getString("content");
		boolean isPositive = row.getBoolean("isPositive");
		Long userId = row.getLong("user_id");
		Long filmId = row.getLong("film_id");
		Long useful = row.getLong("useful");

		return new Review(id, content, isPositive, userId, filmId, useful);
	}
}
