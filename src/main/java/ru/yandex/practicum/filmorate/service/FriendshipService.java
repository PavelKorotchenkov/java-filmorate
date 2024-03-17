package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FriendshipService {
	private final UserStorage userStorage;
	private final FriendshipStorage friendshipStorage;
	private final FeedStorage feedStorage;

	@Autowired
	public FriendshipService(UserStorage userStorage, FriendshipStorage friendshipStorage, FeedStorage feedStorage) {
		this.userStorage = userStorage;
		this.friendshipStorage = friendshipStorage;
		this.feedStorage = feedStorage;
	}

	public void add(Long userId, Long friendId) {
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		User friend = userStorage.findById(friendId);
		if (friend == null) {
			throw new NotFoundException("Пользователь с id " + friendId + " не найден");
		}
		friendshipStorage.add(user.getId(), friend.getId());
		feedStorage.add(userId, friendId, EventOperation.ADD.name(), EventType.FRIEND.name());
	}

	public List<User> getAll(Long id) {
		User user = userStorage.findById(id);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + id + " не найден");
		}
		return friendshipStorage.findAll(user.getId());
	}

	public List<User> showMutual(Long userId, Long friendId) {
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		User friend = userStorage.findById(friendId);
		if (friend == null) {
			throw new NotFoundException("Пользователь с id " + friendId + " не найден");
		}
		return friendshipStorage.findMutual(user.getId(), friend.getId());
	}

	public void delete(Long userId, Long friendId) {
		User user = userStorage.findById(userId);
		if (user == null) {
			throw new NotFoundException("Пользователь с id " + userId + " не найден");
		}
		User friend = userStorage.findById(friendId);
		if (friend == null) {
			throw new NotFoundException("Пользователь с id " + friendId + " не найден");
		}
		friendshipStorage.delete(user.getId(), friend.getId());
		feedStorage.add(userId, friendId, EventOperation.REMOVE.name(), EventType.FRIEND.name());
	}
}
