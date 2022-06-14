package com.example.notice.controller.dto;

import com.example.notice.domain.user.entity.Age;
import com.example.notice.domain.user.entity.Email;
import com.example.notice.domain.user.entity.NickName;
import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserDto {

  @Getter
  public static class UserSignUpRequest {

    @JsonProperty("user")
    private Request request;

    public User convert() {
      return request.convert();
    }

    @Getter
    public static class Request {

      private String username;
      private String email;
      private String password;

      public User convert() {
        return User.createOf(new Username(username),
            new Email(email),
            new Password(password));
      }
    }
  }

  @Getter
  public static class UserLoginRequest {

    private String username;
    private String password;
  }

  @Getter
  public static class ProfileCreateRequest {

    @JsonProperty("profile")
    private Request request;

    public Profile convert() {
      return request.convert();
    }

    @Getter
    public static class Request {

      private String nickname;
      private int age;

      public Profile convert() {
        return Profile.of(new NickName(nickname),
            new Age(age));
      }

    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserResponse {

    @JsonProperty("user")
    private Response response;

    public static UserResponse convert(final User user) {
      return new UserResponse(Response.convert(user));
    }

    @Getter
    public static class Response {

      private final String username;
      private final String email;

      @JsonProperty("profile")
      private final ProfileResponse.Response response;

      @Builder(access = AccessLevel.PRIVATE)
      public Response(final String username, final String email,
          ProfileResponse.Response response) {
        this.username = username;
        this.email = email;
        this.response = response;
      }

      public static Response convert(final User user) {
        return Response.builder()
            .username(Username.toString(user.getUsername()))
            .email(Email.toString(user.getEmail()))
            .response(ProfileResponse.Response.convert(user.getProfile()))
            .build();
      }

    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ProfileResponse {

    @JsonProperty("profile")
    private final Response response;

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class Response {

      private String nickname;
      private int age;

      public static Response convert(final Profile profile) {
        return Response.builder()
            .nickname(NickName.toString(profile.getNickName()))
            .age(Age.toInt(profile.getAge()))
            .build();
      }

    }
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserTokenResponse {

    private String token;

    @JsonProperty("user")
    private Response response;

    public static UserTokenResponse convert(final String token, final User user) {
      return new UserTokenResponse(token, Response.convert(user));
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class Response {

      private String username;
      private String email;

      public static Response convert(final User user) {
        return Response.builder()
            .username(Username.toString(user.getUsername()))
            .email(Email.toString(user.getEmail()))
            .build();
      }
    }

  }

}
