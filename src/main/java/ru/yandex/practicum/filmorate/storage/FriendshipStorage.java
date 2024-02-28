package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface FriendshipStorage {
	boolean addFriend(Long userId, Long friendId);

	void deleteFriend(Long userId, Long friendId);

	List<User> findAllFriends(Long id);

	List<User> findAllMutualFriends(Long userId, Long friendId);
}
