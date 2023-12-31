package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import java.time.LocalDate;

@Data
public class User {
	private int id;

	@Email(message = "Should have Email format")
	@NotBlank(message = "Name cannot be empty")
	private String email;

	@NoSpaces
	@NotBlank(message = "Login cannot be empty")
	private String login;

	private String name;

	@Past(message = "Date of Birth cannot be after today")
	@NotNull(message = "Date of Birth cannot be empty")
	private LocalDate birthday;
}
