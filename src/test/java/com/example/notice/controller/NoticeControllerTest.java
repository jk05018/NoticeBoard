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

@DisplayName("???????????? ???????????? ?????????")
@Transactional
public class NoticeControllerTest extends BasicControllerTest {

  private final String BASE_URI = getBaseUrl(NoticeController.class);

  @Test
  void ????????????_?????????_????????????_??????() throws Exception {
    // given
    ?????????("seunghan1", "pass123");
    ?????????_??????("profile1", 25);

    IntStream.range(0, 3)
        .forEach(count -> ????????????_??????("title" + count, "body" + count));

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
            headerWithName(TOKEN_HEADER).description("JWT ??????")
        ),
        responseFields(
            fieldWithPath("noticeCount").type(NUMBER).description("???????????? ??????"),
            fieldWithPath("notices[].title").type(STRING).description("???????????? ??????"),
            fieldWithPath("notices[].slug").type(STRING).description("???????????? ?????????"),
            fieldWithPath("notices[].body").type(STRING).description("???????????? ??????"),
            fieldWithPath("notices[].writer.nickname").type(STRING).description("????????? ?????????"),
            fieldWithPath("notices[].writer.age").type(NUMBER).description("????????? ??????")
        )));
  }

  @Test
  void ???????????????_????????????_????????????_??????() throws Exception {
    // given
    ?????????("seunghan1", "pass123");
    ?????????_??????("profile1", 25);

    final String title = "title";
    final String body = "body";

    ????????????_??????("title", "body");

    // when
    final ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get(BASE_URI + "/{slug}", Slug.toSlug(title))
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
        pathParameters(
            parameterWithName("slug").description("???????????? ?????????")
        ),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT ??????")
        ),
        responseFields(
            fieldWithPath("notice.title").type(STRING).description("???????????? ??????"),
            fieldWithPath("notice.slug").type(STRING).description("???????????? ?????????"),
            fieldWithPath("notice.body").type(STRING).description("???????????? ??????"),
            fieldWithPath("notice.writer.nickname").type(STRING).description("????????? ?????????"),
            fieldWithPath("notice.writer.age").type(NUMBER).description("????????? ??????")
        )));
  }

  @Test
  void ???????????????_????????????_??????() throws Exception {
    // given
    ?????????("seunghan1", "pass123");
    ?????????_??????("profile1", 25);

    ????????????_??????("title", "body");

    final String updateTitle = "updateTitle";
    final String updateBody = "updateBody";

    final ObjectNode objectNode = new ObjectMapper().createObjectNode();
    final ObjectNode notice = objectNode.putObject("notice");
    notice.put("title", updateTitle)
        .put("body", updateBody);

    // when
    final ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.put(BASE_URI + "/{slug}", Slug.toSlug("title"))
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
        pathParameters(
            parameterWithName("slug").description("???????????? ?????????")
        ),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT ??????")
        ),
        requestFields(
            fieldWithPath("notice.title").type(STRING).description("????????? ???????????? ??????"),
            fieldWithPath("notice.body").type(STRING).description("????????? ???????????? ??????")
        ),
        responseFields(
            fieldWithPath("notice.title").type(STRING).description("???????????? ??????"),
            fieldWithPath("notice.slug").type(STRING).description("???????????? ?????????"),
            fieldWithPath("notice.body").type(STRING).description("???????????? ??????"),
            fieldWithPath("notice.writer.nickname").type(STRING).description("????????? ?????????"),
            fieldWithPath("notice.writer.age").type(NUMBER).description("????????? ??????")
        )));
  }


  @Test
  void ???????????????_????????????_??????() throws Exception {
    // given
    ?????????("seunghan1", "pass123");
    ?????????_??????("profile1", 25);

    final String title = "title";
    final String slug = Slug.toSlug(title);

    ????????????_??????(title, "body");

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
            parameterWithName("slug").description("???????????? ?????????")
        ),
        requestHeaders(
            headerWithName(TOKEN_HEADER).description("JWT ??????")
        )));

  }

  @Test
  void ????????????_?????????_????????????_?????????_????????????_??????() throws Exception {
    // given
    ?????????("seunghan1", "pass123");
    ?????????_??????("profile1", 25);

    final String title = "title";
    final String body = "body";

    IntStream.range(0, 2)
        .forEach(count -> ????????????_??????(title + count, body + count));

    ?????????("seunghan2", "pass123");
    ?????????_??????("profile2", 25);

    IntStream.range(0, 3)
        .forEach(count -> ????????????_??????(count + title, count + body));

    ?????????("seunghan1", "pass123");

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
            headerWithName(TOKEN_HEADER).description("JWT ??????")
        ),
        responseFields(
            fieldWithPath("noticeCount").type(NUMBER).description("???????????? ??????"),
            fieldWithPath("notices[].title").type(STRING).description("???????????? ??????"),
            fieldWithPath("notices[].slug").type(STRING).description("???????????? ?????????"),
            fieldWithPath("notices[].body").type(STRING).description("???????????? ??????"),
            fieldWithPath("notices[].writer.nickname").type(STRING).description("????????? ?????????"),
            fieldWithPath("notices[].writer.age").type(NUMBER).description("????????? ??????")
        )));
  }

  private void ????????????_??????(final String title, final String body) {
    try {
      System.out.println("????????? ???????????? ?????? ??????");

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
              headerWithName(TOKEN_HEADER).description("JWT ??????")
          ),
          requestFields(
              fieldWithPath("notice.title").type(STRING).description("????????? ???????????? ??????"),
              fieldWithPath("notice.body").type(STRING).description("????????? ???????????? ??????")
          ),
          responseFields(
              fieldWithPath("notice.title").type(STRING).description("???????????? ??????"),
              fieldWithPath("notice.slug").type(STRING).description("???????????? ?????????"),
              fieldWithPath("notice.body").type(STRING).description("???????????? ??????"),
              fieldWithPath("notice.writer.nickname").type(STRING).description("????????? ?????????"),
              fieldWithPath("notice.writer.age").type(NUMBER).description("????????? ??????")
          )));

    } catch (Exception e) {
      throw new NoticeProjectException("????????? ???????????? ?????? ??????", e);
    }
  }


}
