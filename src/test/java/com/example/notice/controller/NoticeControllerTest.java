package com.example.notice.controller;

import static com.example.notice.util.Tokens.TOKEN_HEADER;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@DisplayName("공지사항 컨트롤러 테스트")
@Transactional
public class NoticeControllerTest extends BasicControllerTest {

  private final String BASE_URI = getBaseUrl(NoticeController.class);

  @Test
  void 공지사항을_슬러지로_조회할수_있다() throws Exception {
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    final String title = "title";
    final String body = "body";

    공지사항_등록("title", "body");

    mockMvc.perform(get(UriComponentsBuilder.fromUriString(BASE_URI)
            .pathSegment(Slug.toSlug(title))
            .build()
            .toUri())
            .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notice.title").value(title))
        .andExpect(jsonPath("$.notice.slug").value(Slug.toSlug(title)))
        .andExpect(jsonPath("$.notice.body").value(body))
        .andExpect(jsonPath("$.notice.writer").exists());

  }

  @Test
  void 공지사항_목록을_조회할수_있다() throws Exception {
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    IntStream.range(0, 30)
        .forEach(count -> 공지사항_등록("title" + count, "body" + count));

    mockMvc.perform(get(UriComponentsBuilder.fromUriString(BASE_URI)
            .build()
            .toUri())
            .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notices[*].title").exists())
        .andExpect(jsonPath("$.notices[*].slug").exists())
        .andExpect(jsonPath("$.notices[*].body").exists())
        .andExpect(jsonPath("$.notices[*].writer").exists())
        .andExpect(jsonPath("$.noticeCount").value(30));
  }


  @Test
  void 공지사항을_수정할수_있다() throws Exception {
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    공지사항_등록("title", "body");

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
            .header(TOKEN_HEADER, getToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectNode.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notice.title").value(updateTitle))
        .andExpect(jsonPath("$.notice.slug").value(Slug.toSlug("title")))
        .andExpect(jsonPath("$.notice.body").value(updateBody))
        .andExpect(jsonPath("$.notice.writer.nickname").value("profile1"))
        .andExpect(jsonPath("$.notice.writer.age").value(25));
  }


  @Test
  void 공지사항을_삭제할수_있다() throws Exception {
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    final String title = "title";
    final String slug = Slug.toSlug(title);

    공지사항_등록(title, "body");

    mockMvc.perform(delete(UriComponentsBuilder.fromUriString(BASE_URI)
            .pathSegment(slug)
            .build()
            .toUri())
            .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk());
  }

  @Test
  void 사용자가_작성한_공지사항_목록을_조회할수_있다() throws Exception {
    로그인("seunghan1", "pass123");
    프로필_등록("profile1", 25);

    final String title = "title";
    final String body = "body";

    IntStream.range(0, 15)
        .forEach(count -> 공지사항_등록(title + count, body + count));

    로그인("seunghan2", "pass123");
    프로필_등록("profile2", 25);

    IntStream.range(0, 10)
        .forEach(count -> 공지사항_등록(count + title, count + body));

    로그인("seunghan1", "pass123");

    mockMvc.perform(get(UriComponentsBuilder.fromUriString(getBaseUrl(NoticeController.class))
            .pathSegment("write")
            .build()
            .toUri())
            .header(TOKEN_HEADER, getToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.notices[*].title").exists())
        .andExpect(jsonPath("$.notices[*].slug").exists())
        .andExpect(jsonPath("$.notices[*].body").exists())
        .andExpect(jsonPath("$.notices[*].writer").exists())
        .andExpect(jsonPath("$.noticeCount").value(15));
  }

  private void 공지사항_등록(final String title, final String body) {
    try {
      System.out.println("테스트 공지사항 등록 시작");

      ObjectNode objectNode = new ObjectMapper().createObjectNode();
      final ObjectNode notice = objectNode.putObject("notice");
      notice.put("title", title);
      notice.put("body", body);

      mockMvc.perform(post(UriComponentsBuilder.fromUriString(BASE_URI)
              .build()
              .toUri())
              .header(TOKEN_HEADER, getToken())
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectNode.toString()))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.notice.title").value(title))
          .andExpect(jsonPath("$.notice.slug").value(Slug.toSlug(title)))
          .andExpect(jsonPath("$.notice.body").value(body));

    } catch (Exception e) {
      throw new NoticeProjectException("테스트 공지사항 생성 실패", e);
    }
  }


}
