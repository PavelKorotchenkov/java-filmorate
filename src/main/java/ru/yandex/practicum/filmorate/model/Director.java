package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Director {
	private Long id;
	@NotBlank
	@EqualsAndHashCode.Exclude
	private String name;

	public Map<String, Object> toMap() {
		Map<String, Object> values = new HashMap<>();
		values.put("id", id);
		values.put("name", name);
		return values;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Director director = (Director) o;
		return Objects.equals(id, director.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
