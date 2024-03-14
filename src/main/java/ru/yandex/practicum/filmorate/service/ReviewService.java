package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class ReviewService {
	private final UserStorage userStorage;

	private final FilmStorage filmStorage;

	private final ReviewStorage reviewStorage;

	@Autowired
	public ReviewService(UserStorage userStorage, FilmStorage filmStorage, ReviewStorage reviewStorage) {
		this.userStorage = userStorage;
		this.filmStorage = filmStorage;
		this.reviewStorage = reviewStorage;
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
		return reviewStorage.add(review);
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
		return update;
	}

	public void deleteReview(Long reviewId) {
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
		if (filmId != null) {
			Film film = filmStorage.findById(filmId);
			if (film == null) {
				throw new NotFoundException("Фильм с id " + filmId + " не найден");
			}
			return reviewStorage.findAllByFilmId(film.getId(), count);
		}
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
