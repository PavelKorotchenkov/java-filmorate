package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Component
public interface ReviewStorage {
	Review add(Review review);

	Review update(Review review);

	void delete(Long reviewId);

	Review findById(Long reviewId);

	List<Review> findAllByFilmId(Long filmId, int count);

	List<Review> findAll(int count);

	void addLike(Long reviewId, Long userId);

	void addDislike(Long reviewId, Long userId);

	void deleteLike(Long reviewId, Long userId);

	void deleteDislike(Long reviewId, Long userId);
}
