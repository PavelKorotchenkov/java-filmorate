package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
public interface GenreStorage {

	Collection<Genre> findAllGenre();

	Genre findGenreById(Long id);
}
