package com.example.notice.controller.dto;

import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserDto {

	@Getter
	public static class UserSignUpRequest{

		@JsonProperty("user")
		private Request request;

		public User convert(){
			return request.convert();
		}

		@Getter
		public static class Request{
			private String username;
			private String password;

			public User convert(){
				return User.createOf(new Username(username),
								     new Password(password));
			}
		}
	}

	@Getter
	public static class UserLoginRequest{

		private String username;
		private String password;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UserResponse{

		@JsonProperty("user")
		private Response response;

		public static UserResponse convert(final User user){
			 return new UserResponse(Response.convert(user));
		}

		@Getter
		public static class Response{

			private final String username;

			@Builder(access = AccessLevel.PRIVATE)
			public Response(final String username) {
				this.username = username;
			}

			public static Response convert(final User user){
				return Response.builder()
					.username(Username.toString(user.getUsername()))
					.build();
			}
		}
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UserTokenResponse{

		private String token;
		@JsonProperty("user")
		private UserResponse.Response response;

		public static UserTokenResponse convert(final String token, final User user){
			return new UserTokenResponse(token, UserResponse.Response.convert(user));
		}

	}


}
