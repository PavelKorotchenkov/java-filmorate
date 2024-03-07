package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
	private Long reviewId;
	@NotNull
	@NotBlank
	private String content;
	@NotNull
	private Boolean isPositive;
	@NotNull
	private Long userId;
	@NotNull
	private Long filmId;
	private Long useful;

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
