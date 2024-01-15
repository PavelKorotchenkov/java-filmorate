package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
@Component
public interface UserStorage {
	User findUser(Long userId);
	Collection<User> getAllUsers();
	User add(User film);
	User delete(Long userId);
	User update(User film);
}
