package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public interface MpaStorage {
	List<Mpa> findAll();

	Mpa findById(Long id);
}
