package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
	private final Map<Integer, User> users = new HashMap<>();
	private int userId;

	@GetMapping
	public Collection<User> getUsers() {
		return List.copyOf(users.values());
	}

	@PostMapping
	public User addUser(@Valid @RequestBody User user) {
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		user.setId(++userId);
		users.put(user.getId(), user);
		log.info("Added User with id {}", user.getId());
		return user;
	}

	@PutMapping
	public User updateUser(@Valid @RequestBody User user) {
		if (users.containsKey(user.getId())) {
			users.put(user.getId(), user);
			log.info("Updated User with id {}", user.getId());
		} else {
			log.debug("No user in DB with id {}", user.getId());
			throw new ValidationException("No user in DB with id " + user.getId());
		}

		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}

		users.put(user.getId(), user);
		return user;
	}
}
