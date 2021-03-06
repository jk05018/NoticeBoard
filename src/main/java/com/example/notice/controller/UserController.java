package com.example.notice.controller;

import static com.example.notice.controller.dto.UserDto.ProfileCreateRequest;
import static com.example.notice.controller.dto.UserDto.UserLoginRequest;
import static com.example.notice.controller.dto.UserDto.UserResponse;
import static com.example.notice.controller.dto.UserDto.UserSignUpRequest;
import static com.example.notice.controller.dto.UserDto.UserTokenResponse;

import com.example.notice.configuration.jwt.JwtAuthentication;
import com.example.notice.configuration.jwt.JwtAuthenticationToken;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  @PostMapping()
  public ResponseEntity signUp(@RequestBody UserSignUpRequest request) {
    final User user = userService.registerUser(request.convert());

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("profile")
  public ResponseEntity<UserResponse> makeProfile(
      @AuthenticationPrincipal JwtAuthentication authentication,
      @RequestBody ProfileCreateRequest request) {
    final User user = userService.makeProfile(new Username(authentication.username),
        request.convert());

    return new ResponseEntity<>(UserResponse.convert(user), HttpStatus.CREATED);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> me(
      @AuthenticationPrincipal JwtAuthentication authentication) {
    final User user = userService.findByUsername(new Username(authentication.username));

    return new ResponseEntity<>(UserResponse.convert(user), HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<UserTokenResponse> login(@RequestBody UserLoginRequest request) {
    final JwtAuthenticationToken authToken =
        new JwtAuthenticationToken(request.getUsername(), request.getPassword());
    final Authentication resultToken = authenticationManager.authenticate(authToken);
    final JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
    final JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
    final User user = (User) authenticated.getDetails();

    return new ResponseEntity<>(UserTokenResponse.convert(principal.token, user), HttpStatus.OK);
  }

}
