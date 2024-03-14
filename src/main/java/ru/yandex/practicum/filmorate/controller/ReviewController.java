package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

	private final ReviewService reviewService;
	private final FeedService feedService;

	@Autowired
	public ReviewController(ReviewService reviewService, FeedService feedService) {
		this.reviewService = reviewService;
		this.feedService = feedService;
	}

	@PostMapping
	public Review addReview(@Valid @RequestBody Review review) {
		log.info("Получен запрос на добавление отзыва: {}.", review);
		Review result = reviewService.addReview(review);
		feedService.addEvent(result.getUserId(), result.getReviewId(), EventOperation.ADD.name(), EventType.REVIEW.name());
		log.info("Отработан запрос на добавление отзыва: {}.", review);
		return result;
	}

	@PutMapping
	public Review updateReview(@RequestBody Review review) {
		log.info("Получен запрос на обновление отзыва: {}.", review);
		Review result = reviewService.updateReview(review);
		feedService.addEvent(result.getUserId(), result.getReviewId(), EventOperation.UPDATE.name(), EventType.REVIEW.name());
		log.info("Отработан запрос на обновление отзыва.");
		return result;
	}

	@DeleteMapping("/{id}")
	public void deleteReview(@PathVariable("id") Long reviewId) {
		log.info("Получен запрос на удаление отзыва с id: {}.", reviewId);
		Review result = reviewService.getById(reviewId);
		feedService.addEvent(result.getUserId(), reviewId, EventOperation.REMOVE.name(), EventType.REVIEW.name());
		reviewService.deleteReview(reviewId);
		log.info("Отработан запрос на удаление отзыва с id: {}.", reviewId);
	}

	@GetMapping("/{id}")
	public Review getReviewById(@PathVariable("id") Long reviewId) {
		log.info("Получен запрос на получение отзыва с id: {}.", reviewId);
		Review reviewById = reviewService.getById(reviewId);
		log.info("Отработан запрос на получение отзыва с id: {}.", reviewId);
		return reviewById;
	}

	@GetMapping
	public List<Review> getAllReviewsByFilmId(@RequestParam(required = false) Long filmId,
											  @RequestParam(defaultValue = "10") int count) {
		log.info("Получен запрос на получение {} отзывов фильма с id: {}.", count, filmId);
		List<Review> allReviewsByFilmId = reviewService.getAllByFilmId(filmId, count);
		log.info("Отработан запрос на получение отзывов.");
		return allReviewsByFilmId;
	}

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable Long id,
						@PathVariable Long userId) {
		log.info("Получен запрос на добавление лайка фильму с id {} от пользователя с id: {}.", id, userId);
		reviewService.addLike(id, userId);
		log.info("Отработан запрос на добавление лайка фильму с id {} от пользователя с id: {}.", id, userId);
	}

	@PutMapping("/{id}/dislike/{userId}")
	public void addDislike(@PathVariable Long id,
						   @PathVariable Long userId) {
		log.info("Получен запрос на добавление дизлайка фильму с id {} от пользователя с id: {}.", id, userId);
		reviewService.addDislike(id, userId);
		log.info("Отработан запрос на добавление дизлайка фильму с id {} от пользователя с id: {}.", id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public void deleteLike(@PathVariable Long id,
						   @PathVariable Long userId) {
		log.info("Получен запрос на удаление лайка фильму с id {} от пользователя с id: {}.", id, userId);
		reviewService.deleteLike(id, userId);
		log.info("Отработан запрос на удаление лайка фильму с id {} от пользователя с id: {}.", id, userId);
	}

	@DeleteMapping("/{id}/dislike/{userId}")
	public void deleteDislike(@PathVariable Long id,
							  @PathVariable Long userId) {
		log.info("Получен запрос на удаление дизлайка фильму с id {} от пользователя с id: {}.", id, userId);
		reviewService.deleteDislike(id, userId);
		log.info("Отработан запрос на удаление дизлайка фильму с id {} от пользователя с id: {}.", id, userId);
	}
}
