package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	UserStorage userStorage;

	public User addUser(User user) {
		if (!validateUserName(user)) {
			user.setName(user.getLogin());
		}
		return userStorage.add(user);
	}

	public Collection<User> getAllUsers() {
		return userStorage.getAllUsers();
	}

	public User getUserById(Long userId) {
		return userStorage.findUser(userId);
	}

	public User updateUser(User user) {
		return userStorage.update(user);
	}

	public void addFriend(Long friendId, Long userId) {
		User user = userStorage.findUser(userId);
		User friend = userStorage.findUser(friendId);
		if (user != null && friend != null) {
			user.addToFriendList(friend) ;
			friend.addToFriendList(user);
		}
	}

	public void deleteFriend(Long friendId, Long userId) {
		User user = userStorage.findUser(userId);
		User friend = userStorage.findUser(friendId);
		if (user != null && friend != null) {
			user.deleteFromFriendList(friend);
			friend.deleteFromFriendList(user);
		}
	}

	public Set<User> showMutualFriends(Long userId, Long friendId) {
		Set<User> result = new HashSet<>();
		User user = userStorage.findUser(userId);
		User friend = userStorage.findUser(friendId);
		if (user != null && friend != null) {
			result.addAll(user.getFriendList());
			System.out.println(result);
			result.retainAll(friend.getFriendList());
			System.out.println(result);
		}
		return result;
	}

	private boolean validateUserName(User user) {
		return user.getName() != null && !user.getName().isBlank();
	}
}
