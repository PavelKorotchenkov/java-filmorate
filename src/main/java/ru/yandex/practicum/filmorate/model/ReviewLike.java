package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike {
	private Long reviewId;
	private Long userId;
	private boolean isPositive;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReviewLike that = (ReviewLike) o;
		return isPositive == that.isPositive && Objects.equals(reviewId, that.reviewId) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reviewId, userId, isPositive);
	}

	@Override
	public String toString() {
		return "ReviewLike{" +
				"id=" + reviewId +
				", userId=" + userId +
				", isPositive=" + isPositive +
				'}';
	}
}
