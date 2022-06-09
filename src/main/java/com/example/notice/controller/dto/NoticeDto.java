package com.example.notice.controller.dto;

import static com.example.notice.controller.dto.ProfileDto.*;

import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.profile.entity.Profile;
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
			private final String body;
			private final ProfileResponse.Response writer;

			@Builder(access = AccessLevel.PRIVATE)
			public Response(final Title title, final Body body, final Profile writer) {
				this.title = Title.toString(title);
				this.body = Body.toString(body);
				this.writer = ProfileResponse.Response.convert(writer);
			}

			public static Response convert(final Notice notice) {
				return Response.builder()
					.title(notice.getTitle())
					.body(notice.getBody())
					.writer(notice.getWriter())
					.build();
			}
		}

	}
}
