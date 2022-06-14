package com.example.notice.controller.dto;

import static com.example.notice.controller.dto.NoticeDto.NoticeResponse.*;

import java.util.List;
import java.util.stream.Collectors;

import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.user.entity.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class NoticeDto {

  @Getter
  public static class CreateNoticeRequest {

    @JsonProperty("notice")
    private Request request;

    public Notice convert() {
      return request.convert();
    }

    @Getter
    public static class Request {

      private String title;
      private String body;

      public Notice convert() {
        return Notice.createOf(new Title(title),
            new Body(body));
      }
    }
  }

  @Getter
  public static class UpdateNoticeRequest {

    @JsonProperty("notice")
    private Request request;

    public Notice convert() {
      return request.convert();
    }

    @Getter
    public static class Request {

      private String title;
      private String body;

      public Notice convert() {
        return Notice.updateOf(new Title(title),
            new Body(body));
      }
    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class NoticeResponse {

    @JsonProperty("notice")
    private Response response;

    public static NoticeResponse convert(final Notice notice) {
      return new NoticeResponse(Response.convert(notice));
    }

    @Getter
    public static class Response {

      private final String title;
      private final String slug;
      private final String body;
      private final UserDto.ProfileResponse.Response writer;

      @Builder(access = AccessLevel.PRIVATE)
      public Response(final Title title, final Slug slug, final Body body, final Profile writer) {
        this.title = Title.toString(title);
        this.slug = Slug.toString(slug);
        this.body = Body.toString(body);
        this.writer = UserDto.ProfileResponse.Response.convert(writer);
      }

      public static Response convert(final Notice notice) {
        return Response.builder()
            .title(notice.getTitle())
            .slug(notice.getSlug())
            .body(notice.getBody())
            .writer(notice.getWriter())
            .build();
      }

      public static List<Response> convert(final List<Notice> notices) {
        return notices.stream()
            .map(notice -> convert(notice))
            .collect(Collectors.toList());
      }
    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class NoticeResponses {

    @JsonProperty("notices")
    private final List<NoticeResponse.Response> responses;
    private final int noticeCount;

    public static NoticeResponses convert(List<Notice> notices) {
      return new NoticeResponses(Response.convert(notices), notices.size());
    }

  }
}
