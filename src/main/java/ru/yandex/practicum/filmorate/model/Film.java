package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.LocalDateNotBefore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Film {
	private Long id;

	@NotBlank(message = "Name cannot be blank")
	private String name;

	@Size(max = 200, message = "Description size cannot be more than 200 symbols")
	private String description;

	@LocalDateNotBefore
	@NotNull(message = "Release date cannot be empty")
	private LocalDate releaseDate;

	@Positive(message = "Film duration cannot be less than 0")
	@NotNull(message = "Film duration cannot be empty")
	private long duration;

	private Mpa mpa;

	private Set<Genre> genres = new LinkedHashSet<>();

	private Set<Director> directors = new LinkedHashSet<>();

	public Film(Long id, String name, String description, LocalDate releaseDate, long duration, Mpa mpa) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.duration = duration;
		this.mpa = mpa;
	}
}
