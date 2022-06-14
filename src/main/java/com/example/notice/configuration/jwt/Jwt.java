package com.example.notice.configuration.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 암호화처리, verify 등의 기능을 제공 */
@Getter
public final class Jwt {

  private final String issuer;
  private final String clientSecret;
  private final int expirySeconds;
  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public Jwt(String issuer, String clientSecret, int expirySeconds) {
    this.issuer = issuer;
    this.clientSecret = clientSecret;
    this.expirySeconds = expirySeconds;
    this.algorithm = Algorithm.HMAC512(clientSecret);
    this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
        .withIssuer(issuer)
        .build();
  }

  public String sign(Claims claims) {
    Date now = new Date();
    JWTCreator.Builder builder = JWT.create();
    builder.withIssuer(issuer);
    builder.withIssuedAt(now);
    if (expirySeconds > 0) {
      builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
    }
    builder.withClaim("username", claims.username);
    builder.withArrayClaim("roles", claims.roles);
    return builder.sign(algorithm);
  }

  public Claims verify(String token) throws JWTVerificationException {
    return new Claims(jwtVerifier.verify(token));
  }

  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Claims {

    String username;
    String[] roles;
    Date iat;
    Date exp;

    Claims(DecodedJWT decodedJWT) {
      Optional.ofNullable(decodedJWT.getClaim("username"))
          .ifPresent(username -> this.username = username.asString());
      Optional.ofNullable(decodedJWT.getClaim("roles"))
          .ifPresent(roles -> this.roles = roles.asArray(String.class));

      this.iat = decodedJWT.getIssuedAt();
      this.exp = decodedJWT.getExpiresAt();
    }

    public static Claims from(String username, String[] roles) {
      Claims claims = new Claims();
      claims.username = username;
      claims.roles = roles;
      return claims;
    }

    public Map<String, Object> asMap() {
      Map<String, Object> map = new HashMap<>();
      map.put("username", username);
      map.put("roles", roles);
      map.put("iat", iat());
      map.put("exp", exp());
      return map;
    }

    long iat() {
      return iat != null ? iat.getTime() : -1;
    }

    long exp() {
      return exp != null ? exp.getTime() : -1;
    }

    void eraseIat() {
      iat = null;
    }

    void eraseExp() {
      exp = null;
    }
  }

}
