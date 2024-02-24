package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {
	private final UserStorage userStorage;

	@Autowired
	public UserService(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public User addUser(User user) {
		if (!validateUserName(user)) {
			user.setName(user.getLogin());
		}
		return userStorage.save(user);
	}

	public Collection<User> getAllUsers() {
		return userStorage.findAllUsers();
	}

	public User getUserById(Long userId) {
		return userStorage.findUserById(userId);
	}

	public User updateUser(User user) {
		if (!validateUserName(user)) {
			user.setName(user.getLogin());
		}
		return userStorage.update(user);
	}

	private boolean validateUserName(User user) {
		return user.getName() != null && !user.getName().isBlank();
	}
}
