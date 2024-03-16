package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class User {
	private Long id;

	@Email(message = "Should have Email format")
	@NotBlank(message = "Email cannot be empty")
	private String email;

	@NoSpaces
	@NotBlank(message = "Login cannot be empty")
	private String login;

	private String name;

	@Past(message = "Date of Birth cannot be after today")
	@NotNull(message = "Date of Birth cannot be empty")
	private LocalDate birthday;

	@ToString.Exclude
	private List<User> friendList = new ArrayList<>();

	@ToString.Exclude
	private List<Long> likedFilms = new ArrayList<>();

	public User(Long id, String email, String login, String name, LocalDate birthday) {
		this.id = id;
		this.email = email;
		this.login = login;
		this.name = name;
		if (this.name == null || this.name.isBlank()) {
			this.name = login;
		}
		this.birthday = birthday;
	}

	@JsonCreator
	public User(String email, String login, String name, LocalDate birthday) {
		this.email = email;
		this.login = login;
		this.name = name;
		if (this.name == null || this.name.isBlank()) {
			this.name = login;
		}
		this.birthday = birthday;
	}
}
