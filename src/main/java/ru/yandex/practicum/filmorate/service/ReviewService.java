package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class ReviewService {
	private final UserStorage userStorage;

	private final FilmStorage filmStorage;

	private final ReviewStorage reviewStorage;
	private final FeedStorage feedStorage;

	@Autowired
	public ReviewService(UserStorage userStorage, FilmStorage filmStorage, ReviewStorage reviewStorage, FeedStorage feedStorage) {
		this.userStorage = userStorage;
		this.filmStorage = filmStorage;
		this.reviewStorage = reviewStorage;
		this.feedStorage = feedStorage;
	}

	public Review addReview(Review review) {
		User user = userStorage.findById(review.getUserId());
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + review.getUserId() + " не найден");
		}

		Film film = filmStorage.findById(review.getFilmId());
		if (film == null) {
			throw new NotFoundException("Фильм с id " + review.getFilmId() + " не найден");

		}

		Review addedReview = reviewStorage.add(review);
		feedStorage.add(addedReview.getUserId(), addedReview.getReviewId(), EventOperation.ADD.name(), EventType.REVIEW.name());
		return addedReview;
	}

	public Review updateReview(Review review) {
		Review reviewById = reviewStorage.findById(review.getReviewId());
		if (reviewById == null) {
			throw new NotFoundException("Отзыв с id " + review.getReviewId() + " не найден");
		}

		User user = userStorage.findById(review.getUserId());
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + review.getUserId() + " не найден");
		}

		Film film = filmStorage.findById(review.getFilmId());
		if (film == null) {
			throw new NotFoundException("Фильм с id " + review.getFilmId() + " не найден");
		}

		Review update = reviewStorage.update(review);
		feedStorage.add(update.getUserId(), update.getReviewId(), EventOperation.UPDATE.name(), EventType.REVIEW.name());
		return update;
	}

	public void deleteReview(Long reviewId) {
		Review result = reviewStorage.findById(reviewId);
		if (result != null) {
			feedStorage.add(result.getUserId(), reviewId, EventOperation.REMOVE.name(), EventType.REVIEW.name());
		}
		reviewStorage.delete(reviewId);
	}

	public Review getById(Long reviewId) {
		Review byId = reviewStorage.findById(reviewId);
		if (byId == null) {
			throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
		}
		return byId;
	}

	public List<Review> getAllByFilmId(Long filmId, int count) {
		Film film = filmStorage.findById(filmId);
		if (film == null) {
			throw new NotFoundException("Фильм с id " + filmId + " не найден");
		}
		return reviewStorage.findAllByFilmId(film.getId(), count);
	}

	public List<Review> getAll(int count) {
		return reviewStorage.findAll(count);
	}

	public void addLike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		if (review == null) {
			throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
		}
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		reviewStorage.addLike(review.getReviewId(), user.getId());
	}

	public void addDislike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		if (review == null) {
			throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
		}
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		reviewStorage.addDislike(review.getReviewId(), user.getId());
	}

	public void deleteLike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		if (review == null) {
			throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
		}
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		reviewStorage.deleteLike(review.getReviewId(), user.getId());
	}

	public void deleteDislike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		if (review == null) {
			throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
		}
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		reviewStorage.deleteDislike(review.getReviewId(), user.getId());
	}
}
