package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FriendshipService {
	private final UserStorage userStorage;
	private final FriendshipStorage friendshipStorage;

	@Autowired
	public FriendshipService(UserStorage userStorage, FriendshipStorage friendshipStorage) {
		this.userStorage = userStorage;
		this.friendshipStorage = friendshipStorage;
	}

	public void add(Long userId, Long friendId) {
		User user = userStorage.findById(userId); //проверяем, что пользователь есть в базе
		User friend = userStorage.findById(friendId); //проверяем, что друг есть в базе

		friendshipStorage.add(user.getId(), friend.getId());
	}

	public List<User> getAll(Long id) {
		User user = userStorage.findById(id); //проверяем, что пользователь есть в базе
		return friendshipStorage.findAll(user.getId());
	}

	public List<User> showMutual(Long userId, Long friendId) {
		User user = userStorage.findById(userId); //проверяем, что пользователь есть в базе
		User friend = userStorage.findById(friendId); //проверяем, что друг есть в базе

		return friendshipStorage.findMutual(user.getId(), friend.getId());
	}

	public void delete(Long userId, Long friendId) {
		User user = userStorage.findById(userId); //проверяем, что пользователь есть в базе
		User friend = userStorage.findById(friendId); //проверяем, что друг есть в базе

		friendshipStorage.delete(user.getId(), friend.getId());
	}
}
