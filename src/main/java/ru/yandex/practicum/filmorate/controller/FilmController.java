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
		Film film1 = filmService.add(film);
		log.info("Отработан запрос на добавление фильма: {}", film);
		return film1;
	}

	@GetMapping
	public List<Film> getFilms() {
		log.info("Получен запрос на получение всех фильмов");
		List<Film> allFilms = filmService.getAll();
		log.info("Отработан запрос на получение всех фильмов");
		return allFilms;
	}

	@GetMapping("/{filmId}")
	public Film getFilmById(@PathVariable Long filmId) {
		log.info("Получен запрос на получение фильма с id: {}", filmId);
		Film filmById = filmService.getById(filmId);
		log.info("Отработан запрос на получение фильма с id: {}", filmId);
		return filmById;
	}

	@PutMapping
	public Film updateFilm(@Valid @RequestBody Film film) {
		log.info("Получен запрос на изменение фильма: {}", film);
		Film film1 = filmService.update(film);
		log.info("Отработан запрос на изменение фильма: {}", film1);
		return film1;
	}

	@PutMapping("/{filmId}/like/{userId}")
	public void likeFilm(@PathVariable Long filmId,
						 @PathVariable Long userId) {
		log.info("Получен запрос - фильму с id {} поставил лайк пользователь с id {}", filmId, userId);
		likeService.add(filmId, userId);
		log.info("Отработан запрос - фильму с id {} поставил лайк пользователь с id {}", filmId, userId);
	}

	@DeleteMapping("/{filmId}/like/{userId}")
	public void deleteLike(@PathVariable Long filmId,
						   @PathVariable Long userId) {
		log.info("Получен запрос - фильму с id {} убрал лайк пользователь с id {}", filmId, userId);
		likeService.delete(filmId, userId);
		log.info("Отработан запрос - фильму с id {} убрал лайк пользователь с id {}", filmId, userId);
	}

	@GetMapping("/popular")
	public List<Film> getTopRatedFilmsByGenreAndDate(@RequestParam(defaultValue = "10") int count,
													 @RequestParam(required = false) Long genreId,
													 @RequestParam(required = false) Integer year) {
		log.info("Получен запрос на получение топ {} по популярности фильмов", count);
		List<Film> films = filmService.showPopularByGenreAndDate(count, genreId, year);
		log.info("Отработан запрос на получение топ {} по популярности фильмов", count);
		return films;
	}

	@GetMapping("/director/{directorId}")
	public List<Film> getFilmsWithDirector(@PathVariable Long directorId,
										   @RequestParam(defaultValue = "10") String sortBy) {
		log.info("Получен запрос на получение фильмов режиссёра {} с сортировкой по {}", directorId, sortBy);
		List<Film> filmsWithDirector = filmService.getWithDirector(directorId, sortBy);
		log.info("Отработан запрос на получение фильмов режиссёра {} с сортировкой по {}", directorId, sortBy);
		return filmsWithDirector;
	}

	@GetMapping("/common")
	public List<Film> getCommonFilms(@RequestParam Long userId,
									 @RequestParam Long friendId) {
		log.info("Получен запрос на общие фильмы между пользователями {} and {}", userId, friendId);
		List<Film> commonFilms = filmService.getCommon(userId, friendId);
		log.info("Отработан запрос на общие фильмы между пользователями {} and {}", userId, friendId);
		return commonFilms;
	}

	@DeleteMapping("/{id}")
	public void deleteFilm(@PathVariable Long id) {
		log.info("получен запрос на удаление фильма c id {}", id);
		filmService.delete(id);
		log.info("Отработан запрос на удаление фильма c id {}", id);
	}

	@GetMapping("/search")
	public List<Film> smartSearch(@RequestParam String query,
								  @RequestParam String by) {
		log.info("Получен запрос на получение фильма по имени подстроке");
		List<Film> filmBySearch = filmService.getBySearch(query, by);
		log.info("Отработан запрос на получение фильма по имени подстроке");
		return filmBySearch;
	}
}