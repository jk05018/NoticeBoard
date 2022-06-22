package com.example.notice.controller;

import static com.example.notice.document.ApiDocumentUtils.getDocumentRequest;
import static com.example.notice.document.ApiDocumentUtils.getDocumentResponse;
import static com.example.notice.util.Tokens.TOKEN_HEADER;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.exception.NoticeProjectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@DisplayName("공지사항 컨트롤러 테스트")
@Transactional
public class NoticeControllerTest extends BasicControllerTest {

  private final String BASE_URI = getBaseUrl(NoticeController.class);

  @Test
  void 공지사항_목록을_조회할수_있다() throws Exception {
    // given
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    IntStream.range(0, 3)
        .forEach(count -> 공지사항_등록("title" + count, "body" + count));

    // when
    final ResultActions result = mockMvc.perform(
            get(UriComponentsBuilder.fromUriString(BASE_URI)
                .build()
                .toUri())
                .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notices[*].title").exists())
        .andExpect(jsonPath("$.notices[*].slug").exists())
        .andExpect(jsonPath("$.notices[*].body").exists())
        .andExpect(jsonPath("$.notices[*].writer").exists())
        .andExpect(jsonPath("$.noticeCount").value(3));

    // then
    result.andDo(document("notice-list",
        getDocumentRequest(),
        getDocumentResponse(),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT 토큰")
        ),
        responseFields(
            fieldWithPath("noticeCount").type(NUMBER).description("공지사항 개수"),
            fieldWithPath("notices[].title").type(STRING).description("공지사항 제목"),
            fieldWithPath("notices[].slug").type(STRING).description("공지사항 슬러지"),
            fieldWithPath("notices[].body").type(STRING).description("공지사항 내용"),
            fieldWithPath("notices[].writer.nickname").type(STRING).description("작성자 닉네임"),
            fieldWithPath("notices[].writer.age").type(NUMBER).description("작성자 나이")
        )));

  }

  @Test
  void 공지사항을_슬러지로_조회할수_있다() throws Exception {
    // given
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    final String title = "title";
    final String body = "body";

    공지사항_등록("title", "body");

    // when
    final ResultActions result = mockMvc.perform(
            get(UriComponentsBuilder.fromUriString(BASE_URI)
                .pathSegment(Slug.toSlug(title))
                .build()
                .toUri())
                .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notice.title").value(title))
        .andExpect(jsonPath("$.notice.slug").value(Slug.toSlug(title)))
        .andExpect(jsonPath("$.notice.body").value(body))
        .andExpect(jsonPath("$.notice.writer").exists());

    // then
    result.andDo(document("notice-get",
        getDocumentRequest(),
        getDocumentResponse(),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT 토큰")
        ),
        responseFields(
            fieldWithPath("notice.title").type(STRING).description("공지사항 제목"),
            fieldWithPath("notice.slug").type(STRING).description("공지사항 슬러지"),
            fieldWithPath("notice.body").type(STRING).description("공지사항 내용"),
            fieldWithPath("notice.writer.nickname").type(STRING).description("작성자 닉네임"),
            fieldWithPath("notice.writer.age").type(NUMBER).description("작성자 나이")
        )));
  }

  @Test
  void 공지사항을_수정할수_있다() throws Exception {
    // given
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    공지사항_등록("title", "body");

    final String updateTitle = "updateTitle";
    final String updateBody = "updateBody";

    final ObjectNode objectNode = new ObjectMapper().createObjectNode();
    final ObjectNode notice = objectNode.putObject("notice");
    notice.put("title", updateTitle)
        .put("body", updateBody);

    // when
    final ResultActions result = mockMvc.perform(
            put(UriComponentsBuilder.fromUriString(BASE_URI)
                .pathSegment(Slug.toSlug("title"))
                .build()
                .toUri())
                .header(TOKEN_HEADER, getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notice.title").value(updateTitle))
        .andExpect(jsonPath("$.notice.slug").value(Slug.toSlug("title")))
        .andExpect(jsonPath("$.notice.body").value(updateBody))
        .andExpect(jsonPath("$.notice.writer.nickname").value("profile1"))
        .andExpect(jsonPath("$.notice.writer.age").value(25));

    // then
    result.andDo(document("notice-update",
        getDocumentRequest(),
        getDocumentResponse(),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT 토큰")
        ),
        requestFields(
            fieldWithPath("notice.title").type(STRING).description("수정할 공지사항 제목"),
            fieldWithPath("notice.body").type(STRING).description("수정할 공지사항 내용")
        ),
        responseFields(
            fieldWithPath("notice.title").type(STRING).description("공지사항 제목"),
            fieldWithPath("notice.slug").type(STRING).description("공지사항 슬러지"),
            fieldWithPath("notice.body").type(STRING).description("공지사항 내용"),
            fieldWithPath("notice.writer.nickname").type(STRING).description("작성자 닉네임"),
            fieldWithPath("notice.writer.age").type(NUMBER).description("작성자 나이")
        )));
  }


  @Test
  void 공지사항을_삭제할수_있다() throws Exception {
    // given
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    final String title = "title";
    final String slug = Slug.toSlug(title);

    공지사항_등록(title, "body");

    // when
    final ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete(BASE_URI + "/{slug}", slug)
                .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk());

    // then
    result.andDo(document("notice-delete",
        getDocumentRequest(),
        getDocumentResponse(),
        pathParameters(
            parameterWithName("slug").description("공지사항 슬러지")
        ),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT 토큰")
        )));

  }

  @Test
  void 사용자가_작성한_공지사항_목록을_조회할수_있다() throws Exception {
    // given
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    final String title = "title";
    final String body = "body";

    IntStream.range(0, 2)
        .forEach(count -> 공지사항_등록(title + count, body + count));

    로그인("seunghan2", "pass123");
    프로필_등록("profile2", 25);

    IntStream.range(0, 3)
        .forEach(count -> 공지사항_등록(count + title, count + body));

    로그인("seunghan1", "pass123");

    // when
    final ResultActions result = mockMvc.perform(
            get(UriComponentsBuilder.fromUriString(getBaseUrl(NoticeController.class))
                .pathSegment("write")
                .build()
                .toUri())
                .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notices[*].title").exists())
        .andExpect(jsonPath("$.notices[*].slug").exists())
        .andExpect(jsonPath("$.notices[*].body").exists())
        .andExpect(jsonPath("$.notices[*].writer").exists())
        .andExpect(jsonPath("$.noticeCount").value(2));

    // then
    result.andDo(document("notice-write",
        getDocumentRequest(),
        getDocumentResponse(),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT 토큰")
        ),
        responseFields(
            fieldWithPath("noticeCount").type(NUMBER).description("공지사항 개수"),
            fieldWithPath("notices[].title").type(STRING).description("공지사항 제목"),
            fieldWithPath("notices[].slug").type(STRING).description("공지사항 슬러지"),
            fieldWithPath("notices[].body").type(STRING).description("공지사항 내용"),
            fieldWithPath("notices[].writer.nickname").type(STRING).description("작성자 닉네임"),
            fieldWithPath("notices[].writer.age").type(NUMBER).description("작성자 나이")
        )));
  }

  private void 공지사항_등록(final String title, final String body) {
    try {
      System.out.println("테스트 공지사항 등록 시작");

      // given
      ObjectNode objectNode = new ObjectMapper().createObjectNode();
      final ObjectNode notice = objectNode.putObject("notice");
      notice.put("title", title);
      notice.put("body", body);

      // when
      final ResultActions result = mockMvc.perform(
              post(UriComponentsBuilder.fromUriString(BASE_URI)
                  .build()
                  .toUri())
                  .header(TOKEN_HEADER, getToken())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectNode.toString()))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.notice.title").value(title))
          .andExpect(jsonPath("$.notice.slug").value(Slug.toSlug(title)))
          .andExpect(jsonPath("$.notice.body").value(body));

      // then
      result.andDo(document("notice-regist",
          getDocumentRequest(),
          getDocumentResponse(),
          requestHeaders(
              headerWithName(TOKEN_HEADER).description("JWT 토큰")
          ),
          requestFields(
              fieldWithPath("notice.title").type(STRING).description("수정할 공지사항 제목"),
              fieldWithPath("notice.body").type(STRING).description("수정할 공지사항 내용")
          ),
          responseFields(
              fieldWithPath("notice.title").type(STRING).description("공지사항 제목"),
              fieldWithPath("notice.slug").type(STRING).description("공지사항 슬러지"),
              fieldWithPath("notice.body").type(STRING).description("공지사항 내용"),
              fieldWithPath("notice.writer.nickname").type(STRING).description("작성자 닉네임"),
              fieldWithPath("notice.writer.age").type(NUMBER).description("작성자 나이")
          )));

    } catch (Exception e) {
      throw new NoticeProjectException("테스트 공지사항 생성 실패", e);
    }
  }


}
