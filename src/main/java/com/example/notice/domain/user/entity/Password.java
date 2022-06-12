package com.example.notice.domain.user.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

	@Column(nullable = false)
	private String password;

	public Password(final String password) {
		verifyPassword(password);

		this.password = password;
	}

	private void verifyPassword(final String password) {
		if (StringUtils.isEmpty(password)) {
			throw new IllegalArgumentException("password는 null이면 안된다");
		}
	}

	public void encodePassword(final PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(password);
	}

	public static String toString(final Password password) {
		return Optional.ofNullable(password)
			.map(wrapper -> wrapper.password)
			.orElseThrow(IllegalStateException::new);
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
		if (!passwordEncoder.matches(credentials, password)) {
			throw new IllegalArgumentException("Bad credentials");
		}
	}
}
