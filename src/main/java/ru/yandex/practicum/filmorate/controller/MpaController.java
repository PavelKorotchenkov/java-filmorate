package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
	private final MpaService mpaService;

	@Autowired
	public MpaController(MpaService mpaService) {
		this.mpaService = mpaService;
	}

	@GetMapping
	public List<Mpa> getMpas() {
		log.info("Получен запрос на получение всех рейтингов MPA");
		List<Mpa> mpas = mpaService.getMpas();
		log.info("Отработан запрос на получение всех рейтингов MPA");
		return mpas;
	}

	@GetMapping("/{id}")
	public Mpa getMpaById(@PathVariable Long id) {
		log.info("Получен запрос на получение рейтинга MPA с id: {}", id);
		log.info("Отработан запрос на получение рейтинга MPA с id: {}", id);
		return mpaService.getMpaById(id);
	}
}
