package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Genre {
	private Long id;
	private String name;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Genre genre = (Genre) o;
		return Objects.equals(id, genre.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Genre{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
