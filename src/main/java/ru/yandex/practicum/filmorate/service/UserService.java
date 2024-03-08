package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.RecommendationDbStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
	private final UserStorage userStorage;
	private final RecommendationDbStorage recommendationDbStorage;

	@Autowired
	public UserService(UserStorage userStorage, RecommendationDbStorage recommendationDbStorage) {
		this.userStorage = userStorage;
		this.recommendationDbStorage = recommendationDbStorage;
	}

	public User addUser(User user) {
		if (!validateUserName(user)) {
			user.setName(user.getLogin());
			log.info("Имя пользователя не заполнено, присвоено имя: {}", user.getName());
		}
		return userStorage.save(user);
	}

	public List<User> getAllUsers() {
		return userStorage.findAllUsers();
	}

	public User getUserById(Long userId) {
		return userStorage.findUserById(userId);
	}

	public User updateUser(User user) {
		if (!validateUserName(user)) {
			user.setName(user.getLogin());
			log.info("Имя пользователя не заполнено, присвоено имя: {}", user.getName());
		}
		return userStorage.update(user);
	}

	public List<Film> getRecommendation(long id) {
		return recommendationDbStorage.getRecommendation(id);
	}

	private boolean validateUserName(User user) {
		return user.getName() != null && !user.getName().isBlank();
	}
}
