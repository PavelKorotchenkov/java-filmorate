package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
public class DirectorController {
	private final DirectorService directorService;

	public DirectorController(DirectorService directorService) {
		this.directorService = directorService;
	}

	@GetMapping
	public List<Director> getDirectors() {
		log.info("Получен запрос на получение всех режиссёров");
		List<Director> allDirectors = directorService.getAllDirectors();
		log.info("Отработан запрос на получение всех режиссёров");
		return allDirectors;
	}

	@GetMapping("/{directorId}")
	public Director getDirectorById(@PathVariable Long directorId) {
		log.info("Получен запрос на получение режиссёра с id: {}", directorId);
		Director directorById = directorService.getDirectorById(directorId);
		log.info("Отработан запрос на получение режиссёра с id: {}", directorId);
		return directorById;
	}

	@PostMapping
	public Director createDirector(@Valid @RequestBody Director director) {
		log.info("Получен запрос на создание режиссёра: {}", director);
		Director director1 = directorService.createDirector(director);
		log.info("Отработан запрос на создание режиссёра: {}", director);
		return director1;
	}

	@PutMapping
	public Director updateDirector(@Valid @RequestBody Director director) {
		log.info("Получен запрос на изменение режиссёра: {}", director);
		Director director1 = directorService.updateDirector(director);
		log.info("Отработан запрос на изменение режиссёра: {}", director);
		return director1;
	}

	@DeleteMapping("/{directorId}")
	public void deleteDirector(@PathVariable Long directorId) {
		log.info("Получен запрос на удаление режиссёра: {}", directorId);
		directorService.deleteDirector(directorId);
		log.info("Отработан запрос на удаление режиссёра: {}", directorId);
	}
}
