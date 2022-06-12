package com.example.notice.configuration.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigure {

	private String header;
	private String issuer;
	private String clientSecret;
	private int expirySeconds;

}
