package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Genre {
	private Long id;
	private String name;
}
