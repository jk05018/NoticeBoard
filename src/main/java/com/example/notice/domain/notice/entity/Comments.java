package com.example.notice.domain.notice.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.Getter;

@Embeddable
@Getter
public class Comments {

	@OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	public void deleteComment(final Comment comment) {
		this.comments.remove(comment);
	}

	public void addComment(final Comment comment) {
		this.comments.add(comment);

	}
}
