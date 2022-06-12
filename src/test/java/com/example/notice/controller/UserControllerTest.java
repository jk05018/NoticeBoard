package com.example.notice.controller;

import static com.example.notice.util.Tokens.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@DisplayName("유저 컨트롤러 테스트")
@Transactional
class UserControllerTest extends BasicControllerTest {

	@ParameterizedTest
	@CsvSource({
		"user1,1234",
		"user4,pass123"
	})
	void 유저이름과_비밀번호로_유저를_등록할수_있다(final String username, final String password) throws Exception {
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		ObjectNode user = objectNode.putObject("user");
		user.put("username", username)
			.put("password", password);

		mockMvc.perform(post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
				.pathSegment("signup")
				.build()
				.toUri())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectNode.toString()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.user.username").value(username));
	}

	@Test
	void 유저이름과_비밀번호로_로그인할수_있다() throws Exception {
		final String username = "user3";
		final String password = "pass123";

		유저_등록(username, password);

		final ObjectNode objectNode = new ObjectMapper().createObjectNode();
		objectNode.put("username", username)
			.put("password", password);

		mockMvc.perform(post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
				.pathSegment("login")
				.build()
				.toUri())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectNode.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").exists())
			.andExpect(jsonPath("$.user.username").value(username))
			.andReturn().getResponse();
	}

	@Test
	void 토큰으로_유저_정보를_조회할수_있다() throws Exception {
		final String username = "user3";
		final String password = "pass123";

		유저_등록(username, password);
		로그인(username, password);

		mockMvc.perform(get(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
				.pathSegment("me")
				.build()
				.toUri())
				.header(TOKEN_HEADER, getToken()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.user.username").value(username));

	}
}
