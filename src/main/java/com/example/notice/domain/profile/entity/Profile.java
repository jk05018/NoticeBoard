package com.example.notice.domain.profile.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.example.notice.domain.base.BaseIdEntity;
import com.example.notice.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseIdEntity {

	@Embedded
	private NickName nickName;

	@Embedded
	private Email email;

	@Embedded
	private Age age;

	@OneToOne(fetch = FetchType.LAZY)
	private User user;

	private Profile(final NickName nickName, final Email email, final Age age) {
		Optional.ofNullable(nickName).ifPresent(safeNickname -> this.nickName = safeNickname);
		Optional.ofNullable(email).ifPresent(safeEmail -> this.email = safeEmail);
		Optional.ofNullable(age).ifPresent(safeAge -> this.age = safeAge);
	}

	public static Profile of(final NickName nickName,final Email email, final Age age) {
		return new Profile(nickName, email, age);
	}

	public void assignUser(final User user){
		Optional.ofNullable(user).ifPresent(safeUser -> this.user = safeUser);
	}

}
