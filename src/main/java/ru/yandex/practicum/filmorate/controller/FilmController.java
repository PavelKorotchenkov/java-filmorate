package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
	private final FilmService filmService;
	private final LikeService likeService;

	@Autowired
	public FilmController(FilmService filmService, LikeService likeService) {
		this.filmService = filmService;
		this.likeService = likeService;
	}

	@PostMapping
	public Film addFilm(@Valid @RequestBody Film film) {
		return filmService.addFilm(film);
	}

	@GetMapping
	public Collection<Film> getFilms() {
		return filmService.getAllFilms();
	}

	@GetMapping("/{filmId}")
	public Film getFilmById(@PathVariable Long filmId) {
		return filmService.getFilmById(filmId);
	}

	@PutMapping
	public Film updateFilm(@Valid @RequestBody Film film) {
		return filmService.updateFilm(film);
	}

	@PutMapping("/{filmId}/like/{userId}")
	public void likeFilm(@PathVariable Long filmId, @PathVariable Long userId) {
		likeService.addLike(filmId, userId);
	}

	@DeleteMapping("/{filmId}/like/{userId}")
	public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
		likeService.deleteLike(filmId, userId);
	}

	@GetMapping("/popular")
	public Collection<Film> getTopRatedFilms(@RequestParam(defaultValue = "10") int count) {
		return likeService.showPopular(count);
	}
}
