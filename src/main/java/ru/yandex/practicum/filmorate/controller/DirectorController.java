package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
        List<Director> result = directorService.getAll();
        log.info("Получены режиссёры: {}", result);
        return result;
    }

    @GetMapping("/{directorId}")
    public Director getDirectorById(@PathVariable Long directorId) {
        log.info("Получен запрос на получение режиссёра с id: {}", directorId);
        Director result = directorService.getById(directorId);
        log.info("Получен режиссёр: {}", result);
        return result;
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        log.info("Получен запрос на создание режиссёра: {}", director);
        Director result = directorService.create(director);
        log.info("Создан режиссёр: {}", result);
        return result;
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info("Получен запрос на изменение режиссёра: {}", director);
        Director result = directorService.update(director);
        log.info("Изменен режиссёр: {}", result);
        return result;
    }

    @DeleteMapping("/{directorId}")
    public void deleteDirector(@PathVariable Long directorId) {
        log.info("Получен запрос на удаление режиссёра: {}", directorId);
        directorService.delete(directorId);
        log.info("Удален режиссёр с id: {}", directorId);
    }
}
