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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserController controller;

	@MockBean
	private UserService userService;

	@MockBean
	private UserStorage userStorage;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User("email@dat.ru", "Login", "Name", LocalDate.of(2000, 10, 25) );
	}

	@Test
	void whenPostValidUserThenOk() throws Exception {
		String userJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		result.andExpect(status().is2xxSuccessful());
	}

	@Test
	void whenPostUserWithBlankNameThenOk() throws Exception {
		user.setName("");
		String userJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));
		result.andExpect(status().is2xxSuccessful());
	}

	@Test
	void whenPostUserWithBlankEmailThenBadRequest() throws Exception {
		user.setEmail("");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostUserWithInvalidEmailThenBadRequest() throws Exception {
		user.setEmail("email");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostUserWithBlankLoginThenBadRequest() throws Exception {
		user.setLogin("");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostUserWithInvalidLoginThenBadRequest() throws Exception {
		user.setLogin("log in");
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}

	@Test
	void whenPostUserWithInvalidBirthdayDateThenBadRequest() throws Exception {
		user.setBirthday(LocalDate.of(2095, 12, 27));
		String filmJson = objectMapper.writeValueAsString(user);

		ResultActions result = mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(filmJson));

		result.andExpect(status().isBadRequest());
	}
}