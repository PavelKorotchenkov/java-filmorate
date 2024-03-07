package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
	private Long reviewId;
	private String content;
	private boolean isPositive;
	private Long userId;
	private Long filmId;
	private int useful;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Review review = (Review) o;
		return Objects.equals(reviewId, review.reviewId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reviewId);
	}

	@Override
	public String toString() {
		return "Review{" +
				"id=" + reviewId +
				", content='" + content + '\'' +
				", isPositive=" + isPositive +
				", userId=" + userId +
				", filmId=" + filmId +
				", useful=" + useful +
				'}';
	}
}
