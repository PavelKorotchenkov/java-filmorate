package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

	private final JdbcTemplate jdbcTemplate;

	@Test
	void findAllMpas() {
		MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
		List<Mpa> mpas = new ArrayList<>(mpaStorage.findAll());

		Assertions.assertEquals(5, mpas.size());
	}

	@Test
	void findMpaById() {
		MpaStorage mpaStorage = new MpaDbStorage(jdbcTemplate);
		Mpa g = mpaStorage.findById(1L);
		Mpa pg = mpaStorage.findById(2L);
		Mpa pg13 = mpaStorage.findById(3L);
		Mpa r = mpaStorage.findById(4L);
		Mpa nc17 = mpaStorage.findById(5L);

		assertEquals("G", g.getName());
		assertEquals("PG", pg.getName());
		assertEquals("PG-13", pg13.getName());
		assertEquals("R", r.getName());
		assertEquals("NC-17", nc17.getName());
	}
}