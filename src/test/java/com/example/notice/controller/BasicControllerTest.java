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

import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.service.ProfileService;
import com.example.notice.exception.NoticeProjectException;
import com.example.notice.util.Tokens;
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

	@Autowired
	private ProfileService profileService;

	protected Profile profile;

	@BeforeEach
	void setUp() {
		// 프로필_등록("seunghan", "seunghan@naver.com" , 25);
		// profile = profileService.getProfileByNickName(new NickName("seunghan"));
		final String username = "seunghan";
		final String password = "password123";

		유저_등록(username, password);
	}

	private void 프로필_등록(final String nickname, final String email, final int age) {
		try {
			System.out.println("테스트 프로필 등록 시작");
			ObjectNode objectNode = new ObjectMapper().createObjectNode();
			ObjectNode profile = objectNode.putObject("profile");
			profile.put("nickname", nickname)
				.put("email", email)
				.put("age", age);

			mockMvc.perform(post(getBaseUrl(ProfileController.class))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectNode.toString()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.profile.nickname").value(nickname))
				.andExpect(jsonPath("$.profile.email").value(email))
				.andExpect(jsonPath("$.profile.age").value(age));
		} catch (Exception e) {
			throw new NoticeProjectException("테스트 프로필 등록 실패 입니다.", e);
		}
	}

	protected void 유저_등록(final String username, final String password) {
		try {
			System.out.println("테스트 유저 등록 시작");
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
		} catch (Exception e) {
			throw new NoticeProjectException("테스트 유저 등록 실패 입니다.", e);
		}
	}

	protected static String getBaseUrl(final Class<?> clazz) {
		return Stream.of(Optional.ofNullable(AnnotationUtils.findAnnotation(clazz, RequestMapping.class))
				.map(RequestMapping::value)
				.orElseThrow(NullPointerException::new))
			.findFirst()
			.orElseThrow(NullPointerException::new);
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
			throw new NoticeProjectException("유저 로그인 실패입니다.", e);
		}
	}

	protected static String getToken() {
		return loginInfo.get(TOKEN_HEADER).asText();
	}

}
