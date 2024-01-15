package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
	private final FilmService filmService;

	@Autowired
	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping
	public Collection<Film> getFilms() {
		return filmService.getAllFilms();
	}

	@GetMapping("/{filmId}")
	public Film getFilmById(@PathVariable Long filmId) {
		return filmService.getFilmById(filmId);
	}

	@GetMapping("/popular")
	public Set<Film> getTopRatedFilms(@RequestParam(defaultValue = "10") int count) {
		return filmService.showMostLikedFilms(count);
	}

	@PostMapping
	public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
		filmService.addFilm(film);
		return ResponseEntity.status(HttpStatus.CREATED).body(film);
	}

	@PutMapping
	public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
		filmService.updateFilm(film);
		return ResponseEntity.status(HttpStatus.OK).body(film);
	}

	@PutMapping("/{filmId}/like/{userId}")
	public ResponseEntity<Film> likeFilm(@PathVariable Long filmId, @PathVariable Long userId) {
		filmService.addLike(filmId, userId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{filmId}/like/{userId}")
	public ResponseEntity<Film> deleteLikeFilm(@PathVariable Long filmId, @PathVariable Long userId) {
		filmService.deleteLike(filmId, userId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
