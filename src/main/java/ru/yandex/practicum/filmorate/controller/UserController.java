package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping()
	public Collection<User> getUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}

	@GetMapping("{userId}/friends")
	public List<User> getUserFriends(@PathVariable Long userId) {
		return userService.getUserById(userId).getFriendList();
	}

	@GetMapping("{userId}/friends/common/{friendId}")
	public List<User> getMutualUserFriends(@PathVariable Long userId, @PathVariable Long friendId) {
		return userService.showMutualFriends(userId, friendId);
	}

	@PostMapping
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		userService.addUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PutMapping
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
		userService.updateUser(user);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@PutMapping("{userId}/friends/{friendId}")
	public ResponseEntity<User> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
		userService.addFriend(friendId, userId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("{userId}/friends/{friendId}")
	public ResponseEntity<User> deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
		userService.deleteFriend(friendId, userId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}


}
