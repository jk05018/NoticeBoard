package com.example.notice.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.Email;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.exception.NoticeProjectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@DisplayName("공지사항 컨트롤러 테스트")
@Transactional
public class NoticeControllerTest extends BasicControllerTest {

	private final String BASE_URI = getBaseUrl(NoticeController.class);

	@Test
	void 공지사항을_슬러지로_조회할수_있다() throws Exception {
		final String title = "title";
		final String body = "body";

		공지사항_등록("title", "body", profile);

		mockMvc.perform(get(UriComponentsBuilder.fromUriString(BASE_URI)
				.pathSegment(Slug.toSlug(title))
				.build()
				.toUri()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.notice.title", is(title)))
			.andExpect(jsonPath("$.notice.slug", is(Slug.toSlug(title))))
			.andExpect(jsonPath("$.notice.body", is(body)))
			.andExpect(jsonPath("$.notice.writer").exists());

	}

	@Test
	void 공지사항_목록을_조회할수_있다() throws Exception {
		IntStream.range(0, 30)
			.forEach(count -> 공지사항_등록("title" + count, "body" + count, profile));

		mockMvc.perform(get(UriComponentsBuilder.fromUriString(BASE_URI)
				.build()
				.toUri()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.notices[*].title").exists())
			.andExpect(jsonPath("$.notices[*].slug").exists())
			.andExpect(jsonPath("$.notices[*].body").exists())
			.andExpect(jsonPath("$.notices[*].writer").exists())
			.andExpect(jsonPath("$.noticeCount", is(30)));
	}

	@Test
	void 공지사항을_수정할수_있다() throws Exception {
		공지사항_등록("title", "body", profile);

		final String updateTitle = "updateTitle";
		final String updateBody = "updateBody";

		final ObjectNode objectNode = new ObjectMapper().createObjectNode();
		final ObjectNode notice = objectNode.putObject("notice");
		notice.put("title", updateTitle)
			.put("body", updateBody);

		mockMvc.perform(put(UriComponentsBuilder.fromUriString(BASE_URI)
				.pathSegment(Slug.toSlug("title"))
				.build()
				.toUri())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectNode.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.notice.title").value(updateTitle))
			.andExpect(jsonPath("$.notice.slug").value(Slug.toSlug("title")))
			.andExpect(jsonPath("$.notice.body").value(updateBody))
			.andExpect(jsonPath("$.notice.writer.nickname").value(NickName.toString(profile.getNickName())))
			.andExpect(jsonPath("$.notice.writer.email").value(Email.toString(profile.getEmail())))
			.andExpect(jsonPath("$.notice.writer.age").value(Age.toInt(profile.getAge())));
		;

	}

	private void 공지사항_등록(final String title, final String body, final Profile profile) {
		try {
			System.out.println("테스트 공지사항 등록 시작");
			ObjectNode objectNode = new ObjectMapper().createObjectNode();
			final ObjectNode notice = objectNode.putObject("notice");
			notice.put("title", title);
			notice.put("body", body);

			mockMvc.perform(post(UriComponentsBuilder.fromUriString(BASE_URI)
					.pathSegment(String.valueOf(profile.getId()))
					.build()
					.toUri())
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectNode.toString()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.notice.title").value(title))
				.andExpect(jsonPath("$.notice.slug").value(Slug.toSlug(title)))
				.andExpect(jsonPath("$.notice.body").value(body))
				.andExpect(jsonPath("$.notice.writer.nickname").value(NickName.toString(profile.getNickName())))
				.andExpect(jsonPath("$.notice.writer.email").value(Email.toString(profile.getEmail())))
				.andExpect(jsonPath("$.notice.writer.age").value(Age.toInt(profile.getAge())));
		} catch (Exception e) {
			throw new NoticeProjectException("테스트 공지사항 생성 실패", e);
		}
	}
}
