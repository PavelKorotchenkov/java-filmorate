package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
	private final Map<Long, User> users = new HashMap<>();
	private Long userId = 0L;

	@Override
	public User findUser(Long userId) {
		if (!users.containsKey(userId)) {
			throw new NotFoundException("No user in database with id: " + userId);
		}
		return users.get(userId);
	}

	@Override
	public Collection<User> getAllUsers() {
		return List.copyOf(users.values());
	}

	@Override
	public User add(User user) {
		if (nameIsBlank(user)) {
			user.setName(user.getLogin());
		}
		user.setId(++userId);
		return users.put(user.getId(), user);
	}

	@Override
	public User delete(Long userId) {
		if (!users.containsKey(userId)) {
			throw new NotFoundException("No user in database with id: " + userId);
		}
		return users.remove(userId);
	}

	@Override
	public User update(User user) {
		if (!users.containsKey(user.getId())) {
			throw new NotFoundException("No user in database with id: " + user.getId());
		}

		if (nameIsBlank(user)) {
			user.setName(user.getLogin());
		}
		return users.put(user.getId(), user);
	}

	private boolean nameIsBlank(User user) {
		return user.getName() == null || user.getName().isBlank();
	}
}
