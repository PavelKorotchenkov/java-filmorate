package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "reviewId")
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
}
