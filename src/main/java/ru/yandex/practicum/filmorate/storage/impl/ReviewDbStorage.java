package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewReaction;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.util.RowMapper;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Review addReview(Review review) {
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
		log.info("В базу добавлен новый отзыв: " + review);

		return review;
	}

	@Override
	public Review updateReview(Review review) {
		Long id = review.getReviewId();
		findReviewById(id); //проверяем, что отзыв есть в базе
		jdbcTemplate.update("UPDATE reviews " +
						"SET content = ?, isPositive = ? " +
						"WHERE id = ?",
				review.getContent(), review.getIsPositive(), id);

		Review updReview = findReviewById(id);
		log.info("Обновлен отзыв: " + updReview);

		return updReview;
	}

	@Override
	public void deleteReview(Long reviewId) {
		jdbcTemplate.update("DELETE FROM reviews " +
						"WHERE id = ?",
				reviewId);

		log.info("Удален отзыв с id: " + reviewId);
	}

	@Override
	public Review findReviewById(Long reviewId) {
		List<Review> result = jdbcTemplate.query(
				"SELECT * " +
						"FROM reviews " +
						"WHERE id = ?",
				RowMapper::mapRowToReview,
				reviewId
		);
		if (result.size() != 1) {
			log.info("Отзыв с id {} не найден", reviewId);
			throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
		}

		Review review = result.get(0);
		log.info("Найден отзыв с id: {}", reviewId);
		return review;
	}

	@Override
	public List<Review> getAllReviewsByFilmId(Long filmId, int count) {
		log.info("Запрос отзывов к фильму с id: {}", filmId);
		return jdbcTemplate.query(
				"SELECT * FROM reviews " +
						"WHERE film_id = ? " +
						"ORDER BY useful DESC " +
						"LIMIT ?",
				RowMapper::mapRowToReview,
				filmId,
				count);
	}

	@Override
	public List<Review> getAllReviews(int count) {
		log.info("Запрос всех отзывов");
		return jdbcTemplate.query(
				"SELECT * FROM reviews " +
						"ORDER BY useful DESC " +
						"LIMIT ?",
				RowMapper::mapRowToReview,
				count);
	}

	@Override
	public void addLike(Long reviewId, Long userId) {
		List<ReviewReaction> result = jdbcTemplate.query(
				"SELECT review_id, user_id " +
						"FROM reviews_likes " +
						"WHERE review_id = ? " +
						"AND user_id = ?",
				RowMapper::mapRowToReviewReaction,
				reviewId,
				userId
		);

		if (result.isEmpty()) {
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
			log.info("Добавлен лайк отзыву {} от юзера {}", reviewId, userId);
		}
	}

	@Override
	public void addDislike(Long reviewId, Long userId) {
		List<ReviewReaction> result = jdbcTemplate.query(
				"SELECT review_id, user_id " +
						"FROM reviews_likes " +
						"WHERE review_id = ? " +
						"AND user_id = ?",
				RowMapper::mapRowToReviewReaction,
				reviewId,
				userId
		);

		if (result.isEmpty()) {
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

			log.info("Добавлен дизлайк отзыву {} от юзера {}", reviewId, userId);
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

		log.info("Удален лайк отзыву {} от юзера {}", reviewId, userId);
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

		log.info("Удален дизлайк отзыву {} от юзера {}", reviewId, userId);
	}
}
