package com.example.notice.configuration.jwt;

import static com.google.common.base.Preconditions.checkArgument;

import org.springframework.util.StringUtils;

public class JwtAuthentication {

  public final String token;

  public final String username;

  public JwtAuthentication(final String token, final String username) {
    checkArgument(!StringUtils.isEmpty(token), "token must be provided");
    checkArgument(!StringUtils.isEmpty(username), "username must be provided");

    this.token = token;
    this.username = username;
  }
}
