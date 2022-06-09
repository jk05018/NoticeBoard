package com.example.notice.domain.notice.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Body {

	private String body;

	public Body(final String body) {
		verifyBody(body);

		this.body = body;
	}

	private void verifyBody(final String body) {
		if (StringUtils.isEmpty(body)) {
			throw new IllegalArgumentException("Article의 body은 빈 값일 수 없습니다.");
		}
	}

	public static String toString(final Body body){
		return Optional.ofNullable(body)
			.map(wrapper -> wrapper.body)
			.orElseThrow(IllegalStateException::new);
	}
}
