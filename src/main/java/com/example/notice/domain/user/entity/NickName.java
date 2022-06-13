package com.example.notice.domain.user.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NickName {

	@Column(unique = true, length = 10)
	private String nickName;

	public NickName(final String nickName) {
		verifyNickName(nickName);

		this.nickName = nickName;
	}

	public static String toString(final NickName nickName) {
		return Optional.ofNullable(nickName)
			.map(wrapper -> wrapper.nickName)
			.orElseThrow(IllegalStateException::new);
	}

	private void verifyNickName(final String nickName) {
		if (StringUtils.isEmpty(nickName)) {
			throw new IllegalArgumentException("nickname은 1글자 이상 10글자 이하여야 합니다.");
		}
	}
}
