package com.example.notice.domain.user.entity;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import com.example.notice.exception.EmailNotMatchException;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

	/* Email Validation as per RFC2822 standards */
	public static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	@Column(unique = true)
	private String email;

	public Email(final String email) {
		verifyEmail(email);

		this.email = email;
	}

	private void verifyEmail(final String email) {
		if (StringUtils.isEmpty(email)) {
			throw new IllegalArgumentException("이메일은 공백이면 안됩니다. ");
		}

		if (!Pattern.matches(EMAIL_REGEX, email)) {
			throw new EmailNotMatchException();
		}
	}

	public static String toString(final Email email) {
		return Optional.ofNullable(email)
			.map(wrapper -> wrapper.email)
			.orElseThrow(IllegalStateException::new);
	}

}
