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
		userStorage.findUserById(review.getUserId());
		filmStorage.findFilmById(review.getFilmId());

		return reviewStorage.addReview(review);
	}


	public Review updateReview(Review review) {
		userStorage.findUserById(review.getUserId());
		filmStorage.findFilmById(review.getFilmId());

		return reviewStorage.updateReview(review);
	}


	public void deleteReview(Long reviewId) {
		Review review = reviewStorage.findReviewById(reviewId);
		reviewStorage.deleteReview(review.getReviewId());
	}


	public Review getReviewById(Long reviewId) {
		return reviewStorage.findReviewById(reviewId);
	}

	public List<Review> getAllReviewsByFilmId(Long filmId, int count) {
		if (filmId != null) {
			Film film = filmStorage.findFilmById(filmId);
			return reviewStorage.getAllReviewsByFilmId(film.getId(), count);
		}
		return reviewStorage.getAllReviews(count);
	}


	public void addLike(Long reviewId, Long userId) {
		User user = userStorage.findUserById(userId);
		reviewStorage.addLike(reviewId, user.getId());
	}

	public void addDislike(Long reviewId, Long userId) {
		User user = userStorage.findUserById(userId);
		reviewStorage.addDislike(reviewId, user.getId());
	}

	public void deleteLike(Long reviewId, Long userId) {
		User user = userStorage.findUserById(userId);
		reviewStorage.deleteLike(reviewId, user.getId());
	}

	public void deleteDislike(Long reviewId, Long userId) {
		User user = userStorage.findUserById(userId);
		reviewStorage.deleteDislike(reviewId, user.getId());
	}
}
