package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FeedService;
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
	private final FeedService feedService;

	@Autowired
	public FilmController(FilmService filmService, LikeService likeService, FeedService feedService) {
		this.filmService = filmService;
		this.likeService = likeService;
		this.feedService = feedService;
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
        feedService.add(userId, filmId, EventOperation.ADD.name(), EventType.LIKE.name());
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос - фильму с id {} убрал лайк пользователь с id {}", filmId, userId);
        likeService.deleteLike(filmId, userId);
        feedService.add(userId, filmId, EventOperation.REMOVE.name(), EventType.LIKE.name());
    }

	@GetMapping("/popular")
	public List<Film> getTopRatedFilmsByGenreAndDate(@RequestParam(defaultValue = "10") int count,
													 @RequestParam(required = false) Integer genreId,
													 @RequestParam(required = false) Integer year) {
		log.info("Получен запрос на получение топ {} по популярности фильмов", count);
		return filmService.showPopularByGenreAndDate(count, genreId, year);
	}

	@GetMapping("/director/{directorId}")
	public List<Film> getFilmsWithDirector(@PathVariable Long directorId, @RequestParam(defaultValue = "10") String sortBy) {
		log.info("Получен запрос на получение фильмов режиссёра {} с сортировкой по {}", directorId, sortBy);
		return filmService.getFilmsWithDirector(directorId, sortBy);
	}

	@GetMapping("/common")
	public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
		log.info("Получен запрос на общие фильмы между пользователями {} and {}", userId, friendId);
		return filmService.getFilmCommon(userId, friendId);
	}

	@DeleteMapping("/{id}")
	public void deleteFilm(@PathVariable Long id) {
		log.info("получен запрос на удаление фильма c id {}", id);
		filmService.deleteFilm(id);
	}

	@GetMapping("/search")
	public List<Film> smartSearch(@RequestParam String query, @RequestParam String by) {
		log.info("Получен запрос на получение фильма по имени подстроке");
		return filmService.getFilmBySearch(query, by);
	}
}