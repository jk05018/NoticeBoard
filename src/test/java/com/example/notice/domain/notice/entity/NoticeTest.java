package com.example.notice.domain.notice.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.Email;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;

@DisplayName("공지사항 테스트")
class NoticeTest {

	private Profile profile;

	@BeforeEach
	void setUp() {
		profile = Profile.of(new NickName("seunghan"), new Email("writer@email.com"), new Age(25));
	}

	@Test
	void 제목_내용으로_공지사항을_만들수_있다() {
		// given
		final Title title = new Title("this is title");
		final Body body = new Body("this is body");

		// when
		final Notice notice = Notice.createOf(title, body);
		notice.assignWriter(profile);

		// then
		assertThat(notice).extracting(Notice::getTitle, Notice::getBody, Notice::getWriter)
			.isEqualTo(List.of(title, body, profile));
	}

	@ParameterizedTest
	@NullAndEmptySource
	void 제목은_공백이면_안된다(final String nullAndEmptyParam) {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Notice.createOf(new Title(nullAndEmptyParam), new Body("body")));
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Notice.createOf(new Title(""), new Body("body")));
	}

	@ParameterizedTest
	@NullAndEmptySource
	void 내용은_공백이면_안된다(final String nullAndEmptyParam) {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Notice.createOf(new Title("title"), new Body(nullAndEmptyParam)));
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Notice.createOf(new Title("title"), new Body("")));
	}

	@Test
	void 공지사항의_제목_내용을_수정할_수_있다() {
		// given
		final Notice notice = Notice.createOf(new Title("this is title"), new Body("this is body"));

		// when
		final Title updateTitle = new Title("updateTitle");
		final Body updateBody = new Body("updateBody");

		final Notice updateNotice = Notice.updateOf(updateTitle, updateBody);
		notice.updateNotice(updateNotice);

		// then
		assertThat(notice).extracting(Notice::getTitle, Notice::getBody)
			.isEqualTo(List.of(updateTitle, updateBody));
	}
}
