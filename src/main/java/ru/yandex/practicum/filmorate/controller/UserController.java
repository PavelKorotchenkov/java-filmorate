package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
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

    @Autowired
    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        return userService.addUser(user);
    }

    @GetMapping()
    public List<User> getUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        log.info("Получен запрос на получение пользователя с d: {}", userId);
        return userService.getUserById(userId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос - пользователь с id {} добавляет в друзья пользователя с id {}", userId, friendId);
        friendshipService.addFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        log.info("Получен запрос на получение друзей пользователя с id: {}", userId);
        return friendshipService.getFriends(userId);
    }

    @GetMapping("{userId}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на получение общих друзей пользователя с id: {} с пользователем с id: {}", userId, friendId);
        return friendshipService.showMutualFriends(userId, friendId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос - пользователь с id {} удаляет из друзей пользователя с id {}", userId, friendId);
        friendshipService.deleteFriend(userId, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с id {}", id);
        userService.deleteUser(id);
    }
}
