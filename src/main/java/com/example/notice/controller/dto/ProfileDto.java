package com.example.notice.controller.dto;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.Email;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ProfileDto {

	@Getter
	public static class ProfileCreateRequest {

		@JsonProperty("profile")
		private Request request;

		public Profile convert() {
			return this.request.convert();
		}

		@Getter
		public static class Request {

			private String nickname;
			private String email;
			private int age;

			public Profile convert() {
				return Profile.of(new NickName(nickname),
								 new Email(email),
								 new Age(age));
			}
		}

	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ProfileResponse {

		@JsonProperty("profile")
		private final Response response;

		public static ProfileResponse convert(final Profile profile) {
			return new ProfileResponse(Response.convert(profile));
		}

		@Getter
		public static class Response {

			private final String nickname;
			private final String email;
			private final int age;

			@Builder(access = AccessLevel.PRIVATE)
			public Response(final NickName nickname,final Email email, final Age age) {
				this.nickname = NickName.toString(nickname);
				this.email = Email.toString(email);
				this.age = Age.toInt(age);
			}

			public static Response convert(final Profile profile) {
				return Response.builder()
					.nickname(profile.getNickName())
					.email(profile.getEmail())
					.age(profile.getAge())
					.build();
			}

		}
	}
}
