package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface FriendshipStorage {
	boolean add(Long userId, Long friendId);

	void delete(Long userId, Long friendId);

	List<User> findAll(Long id);

	List<User> findMutual(Long userId, Long friendId);
}
