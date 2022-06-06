package com.example.notice.domain.comment.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;

class CommentTest {

	private Profile writer;
	private Notice notice;

	@BeforeEach
	void setUp() {
		writer = Profile.of(new NickName("seung han"), new Age(25));
		notice = Notice.of(new Title("title"), new Body("this is body."), writer);
	}

	@Test
	void 내용과_공지사항_작성자로_댓글을_생성할_수_있다() {
		// given
		final Content content = new Content("this is content");
		final Profile commentWriter = Profile.of(new NickName("commenter"), new Age(30));

		// when
		final Comment comment = Comment.of(content, notice, commentWriter);

		// then
		assertThat(comment)
			.extracting(Comment::getContent, Comment::getNotice, Comment::getWriter)
			.isEqualTo(List.of(content, notice, commentWriter));
	}

	@Test
	public void 내용은_공백이면_안된다() {
		assertThrows(IllegalArgumentException.class,
			() -> Comment.of(new Content(""), notice, Profile.of(new NickName("comment Writer"), new Age(25))));
		assertThrows(IllegalArgumentException.class,
			() -> Comment.of(new Content(null), notice, Profile.of(new NickName("comment Writer"), new Age(25))));
	}

}
