package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.RecommendationStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {

	private final UserStorage userStorage;
	private final RecommendationStorage recommendationStorage;

	@Autowired
	public UserService(UserStorage userStorage, RecommendationStorage recommendationStorage) {
		this.userStorage = userStorage;
		this.recommendationStorage = recommendationStorage;
	}

	public User add(User user) {
		return userStorage.save(user);
	}

	public List<User> getAll() {
		List<User> allUsers = userStorage.findAll();
		return allUsers;
	}


	public User getById(Long userId) {
		User userById = userStorage.findById(userId);
		if (userById == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден.");
		}
		return userById;
	}

	public User update(User user) {
		return userStorage.update(user);
	}

	public void delete(Long id) {
		boolean isDeleted = userStorage.deleteById(id);
		System.err.println(isDeleted);
		if (!isDeleted) {
			throw new NotFoundException("Пользователя не удалось удалить пользователя");
		}
		log.info("Пользователь успешно удален с id {}", id);
	}

	public List<Film> getRecommendation(long id) {
		return recommendationStorage.getById(id);
	}
}
