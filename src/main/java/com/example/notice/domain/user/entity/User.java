package com.example.notice.domain.user.entity;

import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.notice.domain.base.BaseIdEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseIdEntity {

	/* 아이디 */
	@Embedded
	private Username username;

	/* 이메일 */
	@Embedded
	private Email email;

	/* 패스워드 */
	@Embedded
	private Password password;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn
	private Profile profile;

	private User(final Username username, final Email email, final Password password) {
		Optional.ofNullable(username).ifPresent(safeUsername -> this.username = safeUsername);
		Optional.ofNullable(email).ifPresent(safeEmail -> this.email = safeEmail);
		Optional.ofNullable(password).ifPresent(safePassword -> this.password = safePassword);
	}

	public static User createOf(final Username username, final Email email, final Password password) {
		return new User(username, email, password);
	}

	public static User updateOf(final Email email, final Password password) {
		return new User(null, email, password);
	}

	public void assignProfile(final Profile profile) {
		Optional.ofNullable(profile).ifPresent(safeProfile -> this.profile = safeProfile);
	}

	public void updateUser(final User user) {
		Optional.ofNullable(user.email).ifPresent(email -> this.email = email);
		Optional.ofNullable(user.password).ifPresent(password -> this.password = password);
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
		this.password.checkPassword(passwordEncoder, credentials);
	}

}
