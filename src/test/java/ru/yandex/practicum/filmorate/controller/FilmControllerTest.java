package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private FilmController controller;

	@MockBean
	private FilmService filmService;

	@MockBean
	private FilmStorage filmStorage;

	@MockBean
	private NamedParameterJdbcTemplate jdbcTemplate;

	private Film film;

	private final String longDescription = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; //203

	@BeforeEach
	void setUp() {
		film = new Film();
		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(2000, 10, 25));
		film.setDuration(7200000);
	}

	@Test
	void whenPostValidFilmThenOk() throws Exception {
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().is2xxSuccessful());
	}

	@Test
	void whenPostFilmWithEmptyNameThenBadRequest() throws Exception {
		film.setName("");
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostFilmWithBlankNameThenBadRequest() throws Exception {
		film.setName(" ");
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostFilmWithLongDescThenBadRequest() throws Exception {
		film.setDescription(longDescription);
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostFilmWithInvalidReleaseDateThenBadRequest() throws Exception {
		film.setReleaseDate(LocalDate.of(1895, 12, 27));
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostFilmWithDurationZeroThenBadRequest() throws Exception {
		film.setDuration(0);
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostFilmWithNegativeDurationThenBadRequest() throws Exception {
		film.setDuration(-1);
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}
}