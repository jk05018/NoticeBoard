package com.example.notice.domain.user.entity;

import java.util.Optional;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Username {

	@Column(unique = true, length = 30)
	private String username;

	public Username(final String username) {
		verifyUsername(username);

		this.username = username;
	}

	public static String toString(final Username username) {
		return Optional.ofNullable(username)
			.map(wrapper -> wrapper.username)
			.orElseThrow(IllegalStateException::new);
	}

	private void verifyUsername(final String username) {
		if (StringUtils.isEmpty(username)) {
			throw new IllegalArgumentException("username은 빈 값이면 안됩니다.");
		}
	}

}
