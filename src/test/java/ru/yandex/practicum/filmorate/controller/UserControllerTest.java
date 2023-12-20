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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private UserController controller;

	private User user;

	@BeforeEach
	void setUp() {
		controller = new UserController();
		user = new User();
		user.setEmail("email@dat.ru");
		user.setLogin("Login");
		user.setName("Name");
		user.setBirthday(LocalDate.of(2000, 10, 25));
	}

	@Test
	void givenValidUser_whenPost_thenOk() throws Exception {
		String userJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		result.andExpect(status().isCreated());
	}

	@Test
	void givenUserWithBlankName_whenPost_thenOk() throws Exception {
		user.setName("");
		String userJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));
		result.andExpect(status().isCreated());
	}

	@Test
	void givenUserWithBlankEmail_whenPost_thenBadRequest() throws Exception {
		user.setEmail("");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenUserWithInvalidEmail_whenPost_thenBadRequest() throws Exception {
		user.setEmail("email");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenUserWithBlankLogin_whenPost_thenBadRequest() throws Exception {
		user.setLogin("");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenUserWithInvalidLogin_whenPost_thenBadRequest() throws Exception {
		user.setLogin("log in");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void givenFilmWithInvalidReleaseDate_whenPost_thenBadRequest() throws Exception {
		user.setBirthday(LocalDate.of(2095, 12,27));
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}


}