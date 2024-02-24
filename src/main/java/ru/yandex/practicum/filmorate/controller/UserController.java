package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final FriendshipService friendshipService;

	@Autowired
	public UserController(UserService userService, FriendshipService friendshipService) {
		this.userService = userService;
		this.friendshipService = friendshipService;
	}

	@PostMapping
	public User addUser(@Valid @RequestBody User user) {
		return userService.addUser(user);
	}

	@GetMapping()
	public Collection<User> getUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}

	@PutMapping
	public User updateUser(@Valid @RequestBody User user) {
		return userService.updateUser(user);
	}

	@PutMapping("{userId}/friends/{friendId}")
	public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
		friendshipService.addFriend(userId, friendId);
	}

	@GetMapping("{userId}/friends")
	public Collection<User> getFriends(@PathVariable Long userId) {
		return friendshipService.getFriends(userId);
	}

	@GetMapping("{userId}/friends/common/{friendId}")
	public Collection<User> getMutualFriends(@PathVariable Long userId, @PathVariable Long friendId) {
		return friendshipService.showMutualFriends(userId, friendId);
	}

	@DeleteMapping("{userId}/friends/{friendId}")
	public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
		friendshipService.deleteFriend(userId, friendId);
	}
}
