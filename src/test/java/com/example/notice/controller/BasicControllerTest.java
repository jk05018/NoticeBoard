package com.example.notice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.notice.exception.NoticeProjectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
	"logging.level.org.hibernate.SQL=DEBUG",
	"logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
@AutoConfigureMockMvc
public class BasicControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	protected String nickname;
	protected String email;
	protected int age;

	@Before
	public void setUp() throws Exception {
		nickname = "황승한";
		email = "seunghan@naver.com";
		age = 25;

		프로필_등록(nickname, age);
	}

	private void 프로필_등록(final String nickname, final int age) throws Exception {
		try {
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
			throw new NoticeProjectException();
		}
	}

	protected static String getBaseUrl(final Class<?> clazz) {
		return Stream.of(Optional.ofNullable(AnnotationUtils.findAnnotation(clazz, RequestMapping.class))
				.map(RequestMapping::value)
				.orElseThrow(NullPointerException::new))
			.findFirst()
			.orElseThrow(NullPointerException::new);
	}
}
