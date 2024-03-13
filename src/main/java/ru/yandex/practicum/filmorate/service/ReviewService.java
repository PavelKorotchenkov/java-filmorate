package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
		userStorage.findById(review.getUserId());
		filmStorage.findById(review.getFilmId());

		return reviewStorage.add(review);
	}

	public Review updateReview(Review review) {
		userStorage.findById(review.getUserId());
		filmStorage.findById(review.getFilmId());

		return reviewStorage.update(review);
	}

	public void deleteReview(Long reviewId) {
		Review review = reviewStorage.findById(reviewId);
		reviewStorage.delete(review.getReviewId());
	}

	public Review getById(Long reviewId) {
		return reviewStorage.findById(reviewId);
	}

	public List<Review> getAllByFilmId(Long filmId, int count) {
		if (filmId != null) {
			Film film = filmStorage.findById(filmId);
			return reviewStorage.findAllByFilmId(film.getId(), count);
		}
		return reviewStorage.findAll(count);
	}

	public void addLike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		User user = userStorage.findById(userId);
		reviewStorage.addLike(review.getReviewId(), user.getId());
	}

	public void addDislike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		User user = userStorage.findById(userId);
		reviewStorage.addDislike(review.getReviewId(), user.getId());
	}

	public void deleteLike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		User user = userStorage.findById(userId);
		reviewStorage.deleteLike(review.getReviewId(), user.getId());
	}

	public void deleteDislike(Long reviewId, Long userId) {
		Review review = reviewStorage.findById(reviewId);
		User user = userStorage.findById(userId);
		reviewStorage.deleteDislike(review.getReviewId(), user.getId());
	}
}
