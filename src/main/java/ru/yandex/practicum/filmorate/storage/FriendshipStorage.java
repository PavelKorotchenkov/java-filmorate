package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface FriendshipStorage {
	boolean addFriend(Long userId, Long friendId);

	void deleteFriend(Long userId, Long friendId);

	Collection<User> findAllFriends(Long id);

	Collection<User> findAllMutualFriends(Long userId, Long friendId);
}
