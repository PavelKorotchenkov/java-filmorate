package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"friendList", "likedFilms"})
public class User {
	private Long id;

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

	@JsonBackReference
	@ToString.Exclude
	private Set<User> friendList = new LinkedHashSet<>();
	@ToString.Exclude
	private Set<Long> likedFilms = new LinkedHashSet<>();

	public void addToLikedFilms(Long filmId) {
		likedFilms.add(filmId);
	}

	public void deleteFromLikedFilms(Long filmId) {
		likedFilms.remove(filmId);
	}

	public void addToFriendList(User user) {
		friendList.add(user);
	}

	public void deleteFromFriendList(User user) {
		friendList.remove(user);
	}
}
