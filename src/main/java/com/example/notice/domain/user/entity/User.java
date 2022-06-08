package com.example.notice.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.example.notice.domain.base.BaseIdEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "admin")
public class User extends BaseIdEntity {

	private String username;

	private String password;

	private String roles;

	private User(String username, String password, String roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public static User of(String username, String password){
		return new User(username, password, null);
	}
}
