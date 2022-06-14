package com.example.notice.domain.notice.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.example.notice.domain.user.entity.Profile;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class Likes {

	@OneToMany
	@JoinColumn
	private Set<Profile> likes = new HashSet<>();

	public void like(Profile user) {
		likes.add(user);
	}

	public void unlike(Profile user) {
		likes.remove(user);
	}
}
