package com.example.notice.domain.user.entity;

import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
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

	@Embedded
	private Username username;

	@Embedded
	private Password password;

	private User(final Username username, final Password password) {
		Optional.ofNullable(username).ifPresent(safeUsername -> this.username = safeUsername);
		Optional.ofNullable(password).ifPresent(safePassword -> this.password = safePassword);
	}

	public static User createOf(final Username username, final Password password) {
		return new User(username, password);
	}

	public static User updateOf(final Password password) {
		return new User(null, password);
	}

	public void updatePassword(final User user) {
		Optional.ofNullable(user.password).ifPresent(pass -> this.password = pass);
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String credentials){
		this.password.checkPassword(passwordEncoder, credentials);
	}

}
