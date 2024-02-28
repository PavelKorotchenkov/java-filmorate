package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilmLike {
	private Long filmId;
	private Long userId;
	private LocalDate createdAt;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserFilmLike that = (UserFilmLike) o;
		return Objects.equals(filmId, that.filmId) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(filmId, userId);
	}

	@Override
	public String toString() {
		return "UserFilmLike{" +
				"filmId=" + filmId +
				", userId=" + userId +
				", createdAt=" + createdAt +
				'}';
	}
}
