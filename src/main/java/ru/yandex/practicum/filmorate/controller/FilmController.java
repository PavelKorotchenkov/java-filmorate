package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

	private final Map<Integer, Film> films = new HashMap<>();
	private int filmID;

	@GetMapping
	public Collection<Film> getFilms() {
		return List.copyOf(films.values());
	}

	@PostMapping
	public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
		film.setId(++filmID);
		films.put(film.getId(), film);
		log.info("Added Film with id {}", film.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(film);
	}

	@PutMapping
	public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
		if (films.containsKey(film.getId())) {
			films.put(film.getId(), film);
			log.info("Updated Film with id {}", film.getId());
		} else {
			log.debug("No film in DB with id {}", film.getId());
			//return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
			throw new ValidationException("No film in DB with id " + film.getId());
		}

		return ResponseEntity.status(HttpStatus.OK).body(film);
	}
}
