package com.example.notice.controller;

import static com.example.notice.util.Tokens.TOKEN_HEADER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.notice.document.ApiDocumentUtils;
import com.example.notice.exception.NoticeProjectException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
	"logging.level.org.hibernate.SQL=DEBUG",
	"logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "docs.api.com")
public class BasicControllerTest {

	protected static JsonNode loginInfo;

	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		??????_??????("seunghan1", "seunghan1@naver.com", "pass123");
		??????_??????("seunghan2", "seunghan2@naver.com", "pass123");
	}

	protected void ??????_??????(final String username, final String email, final String password) {
		try {
			System.out.println("????????? ?????? ?????? ??????");
			ObjectNode objectNode = new ObjectMapper().createObjectNode();
			ObjectNode user = objectNode.putObject("user");
			user.put("username", username)
				.put("email", email)
				.put("password", password);

			mockMvc.perform(post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
					.build()
					.toUri())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectNode.toString()))
				.andExpect(status().isCreated());
		} catch (Exception e) {
			throw new NoticeProjectException("????????? ?????? ?????? ?????? ?????????.", e);
		}
	}

	protected void ?????????(final String username, final String password) {
		try {
			System.out.println("?????? ????????? ??????");
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
				.andReturn().getResponse();

			loginInfo = new ObjectMapper().readValue(response.getContentAsString(), JsonNode.class);

		} catch (Exception e) {
			throw new NoticeProjectException("????????? ?????? ????????? ???????????????.", e);
		}
	}

	protected void ?????????_??????(final String nickname, final int age) {
		try {
			System.out.println("????????? ????????? ?????? ??????");
			final ObjectNode objectNode = new ObjectMapper().createObjectNode();
			final ObjectNode profile = objectNode.putObject("profile");
			profile.put("nickname", nickname)
				.put("age", age);

			System.out.println(getToken());
			mockMvc.perform(post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
							.pathSegment("profile")
							.build()
							.toUri())
							.header(TOKEN_HEADER, getToken())
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectNode.toString()))
					.andExpect(status().isCreated());

		} catch (Exception e) {
			throw new NoticeProjectException("????????? ????????? ?????? ???????????????.", e);
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
