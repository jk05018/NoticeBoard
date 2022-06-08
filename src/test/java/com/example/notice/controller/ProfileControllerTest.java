package com.example.notice.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.notice.domain.profile.service.ProfileService;

public class ProfileControllerTest extends BasicControllerTest {

	private static final String BASE_URI = getBaseUrl(ProfileController.class);

	@Test
	public void 닉네임으로_프로필을_조회할_수_있다() throws Exception {
		mockMvc.perform(get(UriComponentsBuilder.fromUriString(BASE_URI)
							.pathSegment(nickname)
							.build()
							.toUri()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.profile.nickname").value(nickname))
			.andExpect(jsonPath("$.profile.email").value(email))
			.andExpect(jsonPath("$.profile.age").value(age));
	}

	@Test
	public void 닉네임으로_프로필을_삭제할_수_있다() throws Exception {
		mockMvc.perform(delete(UriComponentsBuilder.fromUriString(BASE_URI)
						.pathSegment(nickname)
						.build()
						.toUri()))
			.andExpect(status().isOk());
	}
}
