package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
/*
FilmController
 */
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
		log.info("Получен запрос на добавление фильма: {}", film);
		return filmService.addFilm(film);
	}

	@GetMapping
	public List<Film> getFilms() {
		log.info("Получен запрос на получение всех фильмов");
		return filmService.getAllFilms();
	}

	@GetMapping("/{filmId}")
	public Film getFilmById(@PathVariable Long filmId) {
		log.info("Получен запрос на получение фильма с id: {}", filmId);
		return filmService.getFilmById(filmId);
	}

	@PutMapping
	public Film updateFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на изменение фильма: {}", film);
		return filmService.updateFilm(film);
	}

	@PutMapping("/{filmId}/like/{userId}")
	public void likeFilm(@PathVariable Long filmId, @PathVariable Long userId) {
		log.info("Получен запрос - фильму с id {} поставил лайк пользователь с id {}", filmId, userId);
		likeService.addLike(filmId, userId);
	}

	@DeleteMapping("/{filmId}/like/{userId}")
	public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
		log.info("Получен запрос - фильму с id {} убрал лайк пользователь с id {}", filmId, userId);
		likeService.deleteLike(filmId, userId);
	}

	@GetMapping("/popular")
	public List<Film> getTopRatedFilms(@RequestParam(defaultValue = "10") int count) {
		log.info("Получен запрос на получение топ {} по популярности ильмов", count);
		return filmService.showPopular(count);
	}

	@GetMapping("/common")
	public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
		log.info("Получен запрос на общие фильмы между пользователями {} and {}", userId, friendId);
		return filmService.getFilmCommon(userId, friendId);
	}
}