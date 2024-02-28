package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mpa {
	private Long id;
	private String name;

	public Mpa(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Mpa mpa = (Mpa) o;
		return Objects.equals(id, mpa.id) && Objects.equals(name, mpa.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public String toString() {
		return "Mpa{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
