package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	private final UserService userService;
	private final FriendshipService friendshipService;
	private final FeedService feedService;

	@Autowired
	public UserController(UserService userService, FriendshipService friendshipService, FeedService feedService) {
		this.userService = userService;
		this.friendshipService = friendshipService;
		this.feedService = feedService;
	}

	@PostMapping
	public User addUser(@Valid @RequestBody User user) {
		log.info("Получен запрос на добавление пользователя: {}", user);
		User user1 = userService.addUser(user);
		log.info("Отработан запрос на добавление пользователя: {}", user);
		return user1;
	}

	@GetMapping()
	public List<User> getUsers() {
		log.info("Получен запрос на получение всех пользователей");
		List<User> allUsers = userService.getAllUsers();
		log.info("Отработан запрос на получение всех пользователей");
		return allUsers;
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Long userId) {
		log.info("Получен запрос на получение пользователя с d: {}", userId);
		User userById = userService.getUserById(userId);
		log.info("Отработан запрос на получение пользователя с d: {}", userId);
		return userById;
	}

	@PutMapping
	public User updateUser(@Valid @RequestBody User user) {
		log.info("Получен запрос на обновление пользователя: {}", user);
		User updatedUser = userService.updateUser(user);
		log.info("Отработан запрос на обновление пользователя: {}", user);
		return updatedUser;
	}

	@PutMapping("{userId}/friends/{friendId}")
	public void addFriend(@PathVariable Long userId,
						  @PathVariable Long friendId) {
		log.info("Получен запрос - пользователь с id {} добавляет в друзья пользователя с id {}", userId, friendId);
		friendshipService.addFriend(userId, friendId);
		feedService.addEvent(userId, friendId, EventOperation.ADD.name(), EventType.FRIEND.name());
		log.info("Отработан запрос - пользователь с id {} добавляет в друзья пользователя с id {}", userId, friendId);
	}

	@GetMapping("{userId}/friends")
	public List<User> getFriends(@PathVariable Long userId) {
		log.info("Получен запрос на получение друзей пользователя с id: {}", userId);
		List<User> friends = friendshipService.getFriends(userId);
		log.info("Отработан запрос на получение друзей пользователя с id: {}", userId);
		return friends;
	}

	@GetMapping("{userId}/friends/common/{friendId}")
	public List<User> getMutualFriends(@PathVariable Long userId,
									   @PathVariable Long friendId) {
		log.info("Получен запрос на получение общих друзей пользователя с id: {} с пользователем с id: {}", userId, friendId);
		List<User> users = friendshipService.showMutualFriends(userId, friendId);
		log.info("Отработан запрос на получение общих друзей пользователя с id: {} с пользователем с id: {}", userId, friendId);
		return users;
	}

	@DeleteMapping("{userId}/friends/{friendId}")
	public void deleteFriend(@PathVariable Long userId,
							 @PathVariable Long friendId) {
		log.info("Получен запрос - пользователь с id {} удаляет из друзей пользователя с id {}", userId, friendId);
		friendshipService.deleteFriend(userId, friendId);
		feedService.addEvent(userId, friendId, EventOperation.REMOVE.name(), EventType.FRIEND.name());
		log.info("Отработан запрос - пользователь с id {} удаляет из друзей пользователя с id {}", userId, friendId);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		log.info("Получен запрос на удаление пользователя с id {}", id);
		userService.deleteUser(id);
		log.info("Отработан запрос на удаление пользователя с id {}", id);
	}

	@GetMapping("{id}/recommendations")
	public List<Film> getRecommendation(@PathVariable long id) {
		log.info("Получен запрос - пользователь с id {} получает список рекомендованных фильмов", id);
		List<Film> recommendation = userService.getRecommendation(id);
		log.info("Отработан запрос - пользователь с id {} получает список рекомендованных фильмов", id);
		return recommendation;
	}

	@GetMapping("{userId}/feed")
	public List<Event> getUserEvents(@PathVariable Long userId) {
		log.info("Получен запрос на получение ленты пользователя с id: {}", userId);
		List<Event> events = feedService.getEvents(userId);
		log.info("Отработан запрос на получение ленты пользователя с id: {}", userId);
		return events;
	}
}
