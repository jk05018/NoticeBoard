package com.example.notice.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.Email;
import com.example.notice.domain.profile.entity.NickName;

@DisplayName("프로필 컨트롤러 테스트")
@Transactional
public class ProfileControllerTest extends BasicControllerTest {

	private static final String BASE_URI = getBaseUrl(ProfileController.class);

	@Test
	public void 닉네임으로_프로필을_조회할_수_있다() throws Exception {
		mockMvc.perform(get(UriComponentsBuilder.fromUriString(BASE_URI)
							.pathSegment(NickName.toString(profile.getNickName()))
							.build()
							.toUri()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.profile.%s", "nickname").value(NickName.toString(profile.getNickName())))
			.andExpect(jsonPath("$.profile.email",is(Email.toString(profile.getEmail()))))
			.andExpect(jsonPath("$.profile.age").value(Age.toInt(profile.getAge())));
	}

	@Test
	public void 닉네임으로_프로필을_삭제할_수_있다() throws Exception {
		mockMvc.perform(delete(UriComponentsBuilder.fromUriString(BASE_URI)
						.pathSegment(NickName.toString(profile.getNickName()))
						.build()
						.toUri()))
			.andExpect(status().isOk());
	}
}
