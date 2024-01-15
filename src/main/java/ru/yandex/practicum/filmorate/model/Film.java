package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.LocalDateNotBefore;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
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

	private Set<Long> likedUsers = new LinkedHashSet<>();

	public void addToLikedUsers(Long userId) {
		likedUsers.add(userId);
	}

	public void deleteFromLikedUsers(Long userId) {
		likedUsers.remove(userId);
	}
}
