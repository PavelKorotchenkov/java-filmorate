package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Component
public interface ReviewStorage {
	Review addReview(Review review);

	Review updateReview(Review review);

	void deleteReview(Long reviewId);

	Review findReviewById(Long reviewId);

	List<Review> getAllReviewsByFilmId(Long filmId, int count);

	List<Review> getAllReviews(int count);

	void addLike(Long reviewId, Long userId);

	void addDislike(Long reviewId, Long userId);

	void deleteLike(Long reviewId, Long userId);

	void deleteDislike(Long reviewId, Long userId);
}
