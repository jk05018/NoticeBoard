package com.example.notice.controller;

import static com.example.notice.document.ApiDocumentUtils.getDocumentRequest;
import static com.example.notice.document.ApiDocumentUtils.getDocumentResponse;
import static com.example.notice.util.Tokens.TOKEN_HEADER;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@DisplayName("유저 컨트롤러 테스트")
@Transactional
class UserControllerTest extends BasicControllerTest {

  @ParameterizedTest
  @CsvSource({
      "user1,hani@email.com,1234",
      "user4,han@naber.com,pass123"
  })
  void 유저이름과_비밀번호로_유저를_등록할수_있다(final String username, final String email, final String password)
    // given
      throws Exception {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        ObjectNode user = objectNode.putObject("user");
        user.put("username", username)
            .put("email", email)
            .put("password", password);

        // when
        final ResultActions result = mockMvc.perform(
                post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
                    .build()
                    .toUri())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectNode.toString()))
            .andExpect(status().isCreated());

        // then
        result.andDo(document("user-register",
            getDocumentRequest(),
            getDocumentResponse(),
            requestFields(
                fieldWithPath("user.username").type(STRING).description("이름"),
                fieldWithPath("user.email").type(STRING).description("이메일"),
                fieldWithPath("user.password").type(STRING).description("비밀번호")
            )));
      }

  @Test
  void 유저이름과_비밀번호로_로그인할수_있다() throws Exception {
    // given
    final String username = "user3";
    final String email = "user3@naver.com";
    final String password = "pass123";

    유저_등록(username, email, password);

    final ObjectNode objectNode = new ObjectMapper().createObjectNode();
    objectNode.put("username", username)
        .put("password", password);

    // when
    final ResultActions result = mockMvc.perform(
            post(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
                .pathSegment("login")
                .build()
                .toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.user.username").value(username))
        .andExpect(jsonPath("$.user.email").value(email));

    // then
    result.andDo(document("user-login",
        getDocumentRequest(),
        getDocumentResponse(),
        requestFields(
            fieldWithPath("username").type(STRING).description("유저 이름"),
            fieldWithPath("password").type(STRING).description("비밀번호")
        ),
        responseFields(
            fieldWithPath("token").type(STRING).description("JWT 토큰"),
            fieldWithPath("user.username").type(STRING).description("유저 이름"),
            fieldWithPath("user.email").type(STRING).description("비밀번호")
        )
    ));

  }

  @Test
  void 토큰으로_유저_정보를_조회할수_있다() throws Exception {
    // given
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    // when
    final ResultActions result = mockMvc.perform(
            get(UriComponentsBuilder.fromUriString(getBaseUrl(UserController.class))
                .pathSegment("me")
                .build()
                .toUri())
                .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("seunghan1"))
        .andExpect(jsonPath("$.user.email").value("seunghan1@naver.com"));

    // then
    result.andDo(document("user-me",
        getDocumentRequest(),
        getDocumentResponse(),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT 토큰")
        ),
        responseFields(
            fieldWithPath("user.username").type(STRING).description("유저 이름"),
            fieldWithPath("user.email").type(STRING).description("비밀번호"),
            fieldWithPath("user.profile.nickname").type(STRING).description("닉네임"),
            fieldWithPath("user.profile.age").type(NUMBER).description("나이")
        )));

  }
}
