package com.example.notice.domain.user.entity;

import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.example.notice.domain.base.BaseIdEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseIdEntity {

	/* 닉네임 */
	@Embedded
	private NickName nickName;

	/* 나이 */
	@Embedded
	private Age age;

	private Profile(final NickName nickName, final Age age) {
		Optional.ofNullable(nickName).ifPresent(safeNickname -> this.nickName = safeNickname);
		Optional.ofNullable(age).ifPresent(safeAge -> this.age = safeAge);
	}

	public static Profile of(final NickName nickName, final Age age) {
		return new Profile(nickName, age);
	}

}
