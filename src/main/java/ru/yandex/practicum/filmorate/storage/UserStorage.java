package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
	User save(User user);

	List<User> findAll();

	User findById(Long userId);

	User update(User user);

	boolean deleteById(Long id);
}
