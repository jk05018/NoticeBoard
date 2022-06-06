package com.example.notice.domain.comment.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

	@Column(length = 50, nullable = false)
	private String content;

	public Content(final String content) {
		verifyContent(content);

		this.content = content;
	}

	private void verifyContent(final String content) {
		if(StringUtils.isEmpty(content) || content.length() > 50){
			throw new IllegalArgumentException("댓글의 길이는 1자 이상 50자 이하여야 합니다.");
		}
	}
}
