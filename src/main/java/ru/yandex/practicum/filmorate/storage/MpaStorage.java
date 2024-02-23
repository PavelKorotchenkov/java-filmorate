package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
public interface MpaStorage {
	Collection<Mpa> findAllMpas();

	Mpa findMpaById(Long id);
}
