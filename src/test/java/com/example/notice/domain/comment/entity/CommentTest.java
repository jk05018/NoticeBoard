package com.example.notice.domain.comment.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.user.entity.Age;
import com.example.notice.domain.user.entity.NickName;
import com.example.notice.domain.user.entity.Profile;

class CommentTest {

	private Profile writer;
	private Notice notice;

	@BeforeEach
	void setUp() {
		writer = Profile.of(new NickName("seung han"), new Age(25));
		notice = Notice.createOf(new Title("title"), new Body("this is body."));
		notice.assignWriter(writer);
	}

	@Test
	void 내용과_공지사항_작성자로_댓글을_생성할_수_있다() {
		// given
		final Content content = new Content("this is content");
		final Profile commentWriter = Profile.of(new NickName("commenter"), new Age(30));

		// when
		final Comment comment = Comment.of(content);
		comment.assignNoticeAndWriter(notice, commentWriter);

		// then
		assertThat(comment)
			.extracting(Comment::getContent, Comment::getNotice, Comment::getWriter)
			.isEqualTo(List.of(content, notice, commentWriter));
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void 내용은_공백이면_안된다(final String nullAndEmpty) {
		assertThrows(IllegalArgumentException.class,
			() -> Comment.of(new Content(nullAndEmpty)));
	}

}
