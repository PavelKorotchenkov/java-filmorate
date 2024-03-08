package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface UserFilmLikeStorage {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getAllCommonFilms(Long userId, Long friendId);

    List<Film> findPopularByGenreAndDate(int count, Integer genreId, Integer year);
}