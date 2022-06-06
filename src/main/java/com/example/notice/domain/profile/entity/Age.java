package com.example.notice.domain.profile.entity;

import java.util.Optional;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Age {

	private int age;

	public Age(final int age) {
		verifyAge(age);

		this.age = age;
	}

	private void verifyAge(final int age) {
		if(age <= 0){
			throw new IllegalArgumentException("나이는 1 이상 이어야 합니다.");
		}
	}

	public static int toInt(final Age age){
		return Optional.ofNullable(age)
			.map(wrapper -> wrapper.age)
			.orElseThrow(IllegalStateException::new);
	}

}
