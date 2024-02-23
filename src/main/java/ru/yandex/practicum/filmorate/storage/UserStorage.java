package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface UserStorage {
	User save(User user);

	Collection<User> findAllUsers();

	User findUserById(Long userId);

	User update(User user);
}
