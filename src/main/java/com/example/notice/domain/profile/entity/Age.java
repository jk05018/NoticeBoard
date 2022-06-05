package com.example.notice.domain.profile.entity;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Age {

	private int age;

	public Age(int age) {
		verifyAge(age);

		this.age = age;
	}

	private void verifyAge(int age) {
		if(age <= 0){
			throw new IllegalArgumentException("나이는 1 이상 이어야 합니다.");
		}
	}

}
