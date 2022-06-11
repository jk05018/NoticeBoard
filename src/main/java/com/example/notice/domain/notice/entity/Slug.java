package com.example.notice.domain.notice.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Slug {

	@Column(unique = true)
	private String slug;

	public Slug(final String slug) {
		this.slug = toSlug(slug);
	}

	public Slug(final Title title){
		this(Title.toString(title));
	}

	public static String toString(final Slug slug) {
		return Optional.ofNullable(slug)
			.map(wrapper -> wrapper.slug)
			.orElseThrow(IllegalStateException::new);
	}

	public  static String toSlug(final String slug) {
		return slug.toLowerCase()
			.replaceAll("\\s", "-");
	}
}
