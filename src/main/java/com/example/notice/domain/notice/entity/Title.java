package com.example.notice.domain.notice.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

	@Column(unique = true, length = 30)
	private String title;

	public Title(String title) {
		verifyTitle(title);

		this.title = title;
	}

	private void verifyTitle(String title) {
		if (StringUtils.isEmpty(title)) {
			throw new IllegalArgumentException("Article의 Title은 빈 값일 수 없습니다.");
		}
	}

}
