package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Genre {
	private Long id;
	private String name;
}
