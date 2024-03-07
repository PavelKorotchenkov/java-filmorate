package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping
	public Review addReview(@RequestBody Review review) {
		return reviewService.addReview(review);
	}

	@PutMapping
	public Review updateReview(@RequestBody Review review) {
		return reviewService.updateReview(review);
	}

	@DeleteMapping("/{id}")
	public void deleteReview(@PathVariable("id") Long reviewId) {
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
	public void addLike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
		reviewService.addLike(reviewId, userId);
	}

	@PutMapping("/{id}/dislike/{userId}")
	public void addDislike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
		reviewService.addDislike(reviewId, userId);
	}
	@DeleteMapping("/{id}/like/{userId}")
	public void deleteLike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
		reviewService.deleteLike(reviewId, userId);
	}

	@DeleteMapping("/{id}/dislike/{userId}")
	public void deleteDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
		reviewService.deleteDislike(reviewId, userId);
	}
}
