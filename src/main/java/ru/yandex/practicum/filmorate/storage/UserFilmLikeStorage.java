package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public interface UserFilmLikeStorage {

	void addLike(Long filmId, Long userId);

	void deleteLike(Long filmId, Long userId);

	Collection<Film> findPopular(int count);
}
