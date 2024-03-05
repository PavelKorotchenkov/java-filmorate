package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;

public class RecommendationDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
