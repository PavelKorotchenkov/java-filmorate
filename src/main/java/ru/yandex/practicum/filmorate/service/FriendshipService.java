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

	public void addFriend(Long userId, Long friendId) {
		User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе
		User friend = userStorage.findUserById(friendId); //проверяем, что друг есть в базе

		friendshipStorage.addFriend(user.getId(), friend.getId());
	}

	public List<User> getFriends(Long id) {
		return friendshipStorage.findAllFriends(id);
	}

	public List<User> showMutualFriends(Long userId, Long friendId) {
		User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе
		User friend = userStorage.findUserById(friendId); //проверяем, что друг есть в базе

		return friendshipStorage.findAllMutualFriends(user.getId(), friend.getId());
	}

	public void deleteFriend(Long userId, Long friendId) {
		User user = userStorage.findUserById(userId); //проверяем, что пользователь есть в базе
		User friend = userStorage.findUserById(friendId); //проверяем, что друг есть в базе

		friendshipStorage.deleteFriend(user.getId(), friend.getId());
	}
}
