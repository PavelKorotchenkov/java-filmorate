package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
	private final GenreService genreService;

	@Autowired
	public GenreController(GenreService genreService) {
		this.genreService = genreService;
	}

	@GetMapping
	public List<Genre> getGenres() {
		log.info("Получен запрос на получение всех жанров");
		List<Genre> genres = genreService.getAll();
		log.info("Отработан запрос на получение всех жанров");
		return genres;
	}

	@GetMapping("/{id}")
	public Genre getGenreById(@PathVariable Long id) {
		log.info("Получен запрос на получение жанра с id: {}", id);
		Genre genreById = genreService.getById(id);
		log.info("Отработан запрос на получение жанра с id: {}", id);
		return genreById;
	}
}
