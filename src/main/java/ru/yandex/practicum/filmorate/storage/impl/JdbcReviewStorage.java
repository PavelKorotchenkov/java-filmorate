package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.util.mapper.MapRowToReview;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Repository
public class JdbcReviewStorage implements ReviewStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JdbcReviewStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Review add(Review review) {
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"INSERT INTO reviews (content, isPositive, user_id, film_id, useful) " +
						"VALUES (?, ?, ?, ?, ?)",
				Types.VARCHAR, Types.BOOLEAN, Types.INTEGER, Types.INTEGER, Types.INTEGER);
		pscf.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc =
				pscf.newPreparedStatementCreator(
						Arrays.asList(
								review.getContent(),
								review.getIsPositive(),
								review.getUserId(),
								review.getFilmId(),
								0));
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		long reviewId = keyHolder.getKey().longValue();
		review.setReviewId(reviewId);

		return review;
	}

	@Override
	public Review update(Review review) {
		Long id = review.getReviewId();
		jdbcTemplate.update("UPDATE reviews " +
						"SET content = ?, isPositive = ? " +
						"WHERE id = ?",
				review.getContent(), review.getIsPositive(), id);

		return findById(id);
	}

	@Override
	public void delete(Long reviewId) {
		jdbcTemplate.update("DELETE FROM reviews " +
						"WHERE id = ?",
				reviewId);
	}

	@Override
	public Review findById(Long reviewId) {
		List<Review> result = jdbcTemplate.query(
				"SELECT * " +
						"FROM reviews " +
						"WHERE id = ?",
				MapRowToReview::map,
				reviewId
		);

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	@Override
	public List<Review> findAllByFilmId(Long filmId, int count) {
		return jdbcTemplate.query(
				"SELECT * FROM reviews " +
						"WHERE film_id = ? " +
						"ORDER BY useful DESC " +
						"LIMIT ?",
				MapRowToReview::map,
				filmId,
				count);
	}

	@Override
	public List<Review> findAll(int count) {
		return jdbcTemplate.query(
				"SELECT * FROM reviews " +
						"ORDER BY useful DESC " +
						"LIMIT ?",
				MapRowToReview::map,
				count);
	}

	@Override
	public void addLike(Long reviewId, Long userId) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) " +
						"FROM reviews_likes " +
						"WHERE review_id = ? AND user_id = ?",
				Integer.class,
				reviewId, userId
		);

		if (count != null && count == 0) {
			jdbcTemplate.update(
					"INSERT INTO reviews_likes (review_id, user_id, isPositive) " +
							"VALUES (?, ?, ?)",
					reviewId, userId, true
			);

			jdbcTemplate.update(
					"UPDATE reviews " +
							"SET useful = useful + 1 " +
							"WHERE id = ?",
					reviewId
			);
		}
	}

	@Override
	public void addDislike(Long reviewId, Long userId) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) " +
						"FROM reviews_likes " +
						"WHERE review_id = ? AND user_id = ?",
				Integer.class,
				reviewId, userId
		);

		if (count != null && count == 0) {
			jdbcTemplate.update(
					"INSERT INTO reviews_likes (review_id, user_id, isPositive) " +
							"VALUES (?, ?, ?)",
					reviewId, userId, false
			);

			jdbcTemplate.update(
					"UPDATE reviews " +
							"SET useful = useful - 1 " +
							"WHERE id = ?",
					reviewId
			);
		}
	}

	@Override
	public void deleteLike(Long reviewId, Long userId) {
		jdbcTemplate.update(
				"DELETE FROM reviews_likes " +
						"WHERE review_id = ? " +
						"AND user_id = ? " +
						"AND isPositive = true;",
				reviewId, userId
		);

		jdbcTemplate.update(
				"UPDATE reviews " +
						"SET useful = useful - 1 " +
						"WHERE id = ?",
				reviewId
		);
	}

	@Override
	public void deleteDislike(Long reviewId, Long userId) {

		jdbcTemplate.update(
				"DELETE FROM reviews_likes " +
						"WHERE review_id = ? " +
						"AND user_id = ? " +
						"AND isPositive = false;",
				reviewId, userId
		);

		jdbcTemplate.update(
				"UPDATE reviews " +
						"SET useful = useful + 1 " +
						"WHERE id = ?",
				reviewId
		);
	}
}
