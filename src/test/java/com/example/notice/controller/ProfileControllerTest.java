package com.example.notice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ProfileControllerTest extends BasicControllerTest{

	private static final String BASE_URL = getBaseUrl(ProfileController.class);

	@Test
	public void 닉네임_나이로_프로필을_생성한다() throws Exception {
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		ObjectNode profile = objectNode.putObject("profile");
		profile.put("nickname","seunghan")
				.put("age",25);

		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectNode.toString()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.profile.nickname").value("seunghan"))
			.andExpect(jsonPath("$.profile.age").value(25));
	}

}
