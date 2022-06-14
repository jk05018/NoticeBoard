package com.example.notice.controller;

import static com.example.notice.util.Tokens.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.notice.exception.NoticeProjectException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
	"logging.level.org.hibernate.SQL=DEBUG",
	"logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
@AutoConfigureMockMvc
public class BasicControllerTest {

	protected static JsonNode loginInfo;

	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		유저_등록("seunghan", "seunghan@naver.com", "pass123");
		로그인("seunghan", "pass123");
		프로필_등록("seunghan2", 25);

	}

	protected void 유저_등록(final String username, final String email, final String password) {
		try {
			System.out.println("테스트 유저 등록 시작");
			ObjectNode objectNode = new ObjectMapper().createObjectNode();
			ObjectNode user = objectNode.putObject("user");
			user.put("username", username)
				.put("email", email)
				.put("password", password);

			mockMvc.perform(post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
					.pathSegment("signup")
					.build()
					.toUri())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectNode.toString()))
				.andExpect(status().isCreated());
		} catch (Exception e) {
			throw new NoticeProjectException("테스트 유저 등록 실패 입니다.", e);
		}
	}

	protected void 로그인(final String username, final String password) {
		try {
			System.out.println("유저 로그인 시작");
			final ObjectNode objectNode = new ObjectMapper().createObjectNode();
			objectNode.put("username", username)
				.put("password", password);

			final MockHttpServletResponse response = mockMvc.perform(
					post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
						.pathSegment("login")
						.build()
						.toUri())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectNode.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.user.username").value(username))
				.andReturn().getResponse();

			loginInfo = new ObjectMapper().readValue(response.getContentAsString(), JsonNode.class);

		} catch (Exception e) {
			throw new NoticeProjectException("테스트 유저 로그인 실패입니다.", e);
		}
	}

	protected void 프로필_등록(final String nickname, final int age) {
		try {
			System.out.println("테스트 프로필 등록 시작");
			final ObjectNode objectNode = new ObjectMapper().createObjectNode();
			final ObjectNode profile = objectNode.putObject("profile");
			profile.put("nickname", nickname)
				.put("age", age);

			mockMvc.perform(post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
					.pathSegment("profile")
					.build()
					.toUri())
					.header(TOKEN_HEADER, getToken())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectNode.toString()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.user.username").value("seunghan"))
				.andExpect(jsonPath("$.user.email").value("seunghan@naver.com"))
				.andExpect(jsonPath("$.user.profile.nickname").value(nickname))
				.andExpect(jsonPath("$.user.profile.age").value(age));

		} catch (Exception e) {
			throw new NoticeProjectException("테스트 프로필 등록 실패입니다.", e);
		}
	}

	protected static String getBaseUrl(final Class<?> clazz) {
		return Stream.of(Optional.ofNullable(AnnotationUtils.findAnnotation(clazz, RequestMapping.class))
				.map(RequestMapping::value)
				.orElseThrow(NullPointerException::new))
			.findFirst()
			.orElseThrow(NullPointerException::new);
	}

	protected static String getToken() {
		return loginInfo.get(TOKEN_HEADER).asText();
	}

}
