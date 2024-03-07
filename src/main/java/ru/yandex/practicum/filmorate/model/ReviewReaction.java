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
public class ReviewReaction {
	private Long reviewId;
	private Long userId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReviewReaction that = (ReviewReaction) o;
		return Objects.equals(reviewId, that.reviewId) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(reviewId, userId);
	}

	@Override
	public String toString() {
		return "ReviewReaction{" +
				"id=" + reviewId +
				", userId=" + userId +
				'}';
	}
}
