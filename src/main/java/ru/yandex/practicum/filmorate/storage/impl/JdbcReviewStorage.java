package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToReview;

import java.util.List;

@Repository
public class JdbcReviewStorage implements ReviewStorage {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcReviewStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Review add(Review review) {
		String sql = "INSERT INTO reviews (content, isPositive, user_id, film_id, useful) " +
				"VALUES (:content, :isPositive, :userId, :filmId, :useful)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("content", review.getContent());
		params.addValue("isPositive", review.getIsPositive());
		params.addValue("userId", review.getUserId());
		params.addValue("filmId", review.getFilmId());
		params.addValue("useful", 0);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameterJdbcTemplate.update(sql, params, keyHolder);

		long generatedId = keyHolder.getKey().longValue();
		review.setReviewId(generatedId);

		return review;
	}

	@Override
	public Review update(Review review) {
		String sql = "UPDATE reviews SET content = :content, isPositive = :isPositive WHERE id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("content", review.getContent());
		params.addValue("isPositive", review.getIsPositive());
		params.addValue("id", review.getReviewId());

		namedParameterJdbcTemplate.update(sql, params);

		return findById(review.getReviewId());
	}

	@Override
	public void delete(Long reviewId) {
		String sql = "DELETE FROM reviews WHERE id = :reviewId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("reviewId", reviewId);
		namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public Review findById(Long reviewId) {
		String sql = "SELECT * FROM reviews WHERE id = :reviewId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("reviewId", reviewId);
		List<Review> result = namedParameterJdbcTemplate.query(sql, params, MapRowToReview::map);
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public List<Review> findAllByFilmId(Long filmId, int count) {
		String sql = "SELECT * FROM reviews " +
				"WHERE film_id = :filmId " +
				"ORDER BY useful DESC " +
				"LIMIT :count";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("filmId", filmId);
		params.addValue("count", count);
		return namedParameterJdbcTemplate.query(sql, params, MapRowToReview::map);
	}

	@Override
	public List<Review> findAll(int count) {
		String sql = "SELECT * FROM reviews ORDER BY useful DESC LIMIT :count";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("count", count);
		return namedParameterJdbcTemplate.query(sql, params, MapRowToReview::map);
	}

	@Override
	public void addLike(Long reviewId, Long userId) {
		Integer count = namedParameterJdbcTemplate.queryForObject(
				"SELECT COUNT(*) " +
						"FROM reviews_likes " +
						"WHERE review_id = :reviewId AND user_id = :userId",
				new MapSqlParameterSource("reviewId", reviewId).addValue("userId", userId),
				Integer.class
		);

		if (count != null && count == 0) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("reviewId", reviewId);
			params.addValue("userId", userId);

			namedParameterJdbcTemplate.update(
					"INSERT INTO reviews_likes (review_id, user_id, isPositive) " +
							"VALUES (:reviewId, :userId, true)",
					params
			);

			namedParameterJdbcTemplate.update(
					"UPDATE reviews " +
							"SET useful = useful + 1 " +
							"WHERE id = :reviewId",
					params
			);
		}
	}

	@Override
	public void addDislike(Long reviewId, Long userId) {
		Integer count = namedParameterJdbcTemplate.queryForObject(
				"SELECT COUNT(*) " +
						"FROM reviews_likes " +
						"WHERE review_id = :reviewId AND user_id = :userId",
				new MapSqlParameterSource("reviewId", reviewId).addValue("userId", userId),
				Integer.class
		);

		if (count != null && count == 0) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("reviewId", reviewId);
			params.addValue("userId", userId);

			namedParameterJdbcTemplate.update(
					"INSERT INTO reviews_likes (review_id, user_id, isPositive) " +
							"VALUES (:reviewId, :userId, false)",
					params
			);

			namedParameterJdbcTemplate.update(
					"UPDATE reviews " +
							"SET useful = useful - 1 " +
							"WHERE id = :reviewId",
					params
			);
		}
	}

	@Override
	public void deleteLike(Long reviewId, Long userId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("reviewId", reviewId);
		params.addValue("userId", userId);

		namedParameterJdbcTemplate.update(
				"DELETE FROM reviews_likes " +
						"WHERE review_id = :reviewId " +
						"AND user_id = :userId " +
						"AND isPositive = true",
				params
		);

		namedParameterJdbcTemplate.update(
				"UPDATE reviews " +
						"SET useful = useful - 1 " +
						"WHERE id = :reviewId",
				params
		);
	}

	@Override
	public void deleteDislike(Long reviewId, Long userId) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("reviewId", reviewId);
		params.addValue("userId", userId);

		namedParameterJdbcTemplate.update(
				"DELETE FROM reviews_likes " +
						"WHERE review_id = :reviewId " +
						"AND user_id = :userId " +
						"AND isPositive = false",
				params
		);

		namedParameterJdbcTemplate.update(
				"UPDATE reviews " +
						"SET useful = useful + 1 " +
						"WHERE id = :reviewId",
				params
		);
	}
}
