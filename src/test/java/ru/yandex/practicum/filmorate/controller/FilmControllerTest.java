package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
@AutoConfigureMockMvc
class FilmControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private FilmController controller;

	private Film film;

	private final String longDescription = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; //201

	@BeforeEach
	void setUp() {
		controller = new FilmController();
		film = new Film();
		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.of(2000, 10, 25));
		film.setDuration(7200000);

	}

	@Test
	void givenValidFilm_whenPost_thenOk() throws Exception {
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isCreated());
	}

	@Test
	void givenFilmWithEmptyName_whenPost_thenBadRequest() throws Exception {
		film.setName("");
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenFilmWithBlankName_whenPost_thenBadRequest() throws Exception {
		film.setName(" ");
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenFilmWithLongDesc_whenPost_thenBadRequest() throws Exception {
		film.setDescription(longDescription);
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenFilmWithInvalidReleaseDate_whenPost_thenBadRequest() throws Exception {
		film.setReleaseDate(LocalDate.of(1895, 12,27));
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenFilmWithInvalidDurationZero_whenPost_thenBadRequest() throws Exception {
		film.setDuration(0);
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenFilmWithInvalidDurationMinus_whenPost_thenBadRequest() throws Exception {
		film.setDuration(-1);
		String filmJson = objectMapper.writeValueAsString(film);

		ResultActions result = mockMvc.perform(post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}
}