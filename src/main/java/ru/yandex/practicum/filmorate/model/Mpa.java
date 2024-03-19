package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Mpa {
	private Long id;
	private String name;

	public Mpa(Long id) {
		this.id = id;
	}
}
