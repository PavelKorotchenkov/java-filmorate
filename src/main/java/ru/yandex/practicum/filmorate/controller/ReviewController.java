package ru.yandex.practicum.filmorate.controller;

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
		Review result = reviewService.addReview(review);
		feedService.addEvent(result.getUserId(), result.getReviewId(), EventOperation.ADD.name(), EventType.REVIEW.name());
		return result;
	}

	@PutMapping
	public Review updateReview(@RequestBody Review review) {
		Review result = reviewService.updateReview(review);
		feedService.addEvent(result.getUserId(), result.getReviewId(), EventOperation.UPDATE.name(), EventType.REVIEW.name());
		return result;
	}

	@DeleteMapping("/{id}")
	public void deleteReview(@PathVariable("id") Long reviewId) {
		Review result = reviewService.getReviewById(reviewId);
		feedService.addEvent(result.getUserId(), reviewId, EventOperation.REMOVE.name(), EventType.REVIEW.name());
		reviewService.deleteReview(reviewId);
	}

	@GetMapping("/{id}")
	public Review getReviewById(@PathVariable("id") Long reviewId) {
		return reviewService.getReviewById(reviewId);
	}

	@GetMapping
	public List<Review> getAllReviewsByFilmId(@RequestParam(required = false) Long filmId,
											  @RequestParam(defaultValue = "10") int count) {
		return reviewService.getAllReviewsByFilmId(filmId, count);
	}

	@PutMapping("/{id}/like/{userId}")
	public void addLike(@PathVariable Long id, @PathVariable Long userId) {
		reviewService.addLike(id, userId);
	}

	@PutMapping("/{id}/dislike/{userId}")
	public void addDislike(@PathVariable Long id, @PathVariable Long userId) {
		reviewService.addDislike(id, userId);
	}

	@DeleteMapping("/{id}/like/{userId}")
	public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
		reviewService.deleteLike(id, userId);
	}

	@DeleteMapping("/{id}/dislike/{userId}")
	public void deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
		reviewService.deleteDislike(id, userId);
	}
}
