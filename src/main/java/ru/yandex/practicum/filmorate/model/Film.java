package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.LocalDateNotBefore;

import java.time.LocalDate;

@Data
public class Film {
	private int id;

	@NotEmpty(message = "Name cannot be empty")
	private String name;

	@Size(max = 200, message = "Description size cannot be more than 200 symbols")
	private String description;

	@LocalDateNotBefore
	@NotNull(message = "Release date cannot be empty")
	private LocalDate releaseDate;

	@Positive(message = "Film duration cannot be less than 0")
	@NotNull(message = "Film duration cannot be empty")
	private long duration;

}
