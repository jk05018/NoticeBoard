package com.example.notice.domain.notice.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.notice.domain.BasicServiceTest;
import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.notice.repository.NoticeRepository;
import com.example.notice.exception.NoticeProjectException;

class NoticeServiceTest extends BasicServiceTest {

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private NoticeRepository noticeRepository;


	@Test
	void 공지사항을_조회할_수_있다() {
		// given
		final Title title = new Title("title");
		final Body body = new Body("body");

		final Notice notice = 공지사항_등록(title, body);

		// when
		final Notice findNotice = noticeService.getNoticeById(notice.getId());

		// then
		assertThat(findNotice.getTitle()).isEqualTo(notice.getTitle());
		assertThat(findNotice.getBody()).isEqualTo(notice.getBody());
		assertThat(findNotice.getWriter()).isEqualTo(profile);

	}

	Notice 공지사항_등록(final Title title, final Body body){
		try{
			final Notice notice = Notice.createOf(title, body);

			final Notice savedNotice = noticeService.createNotice(notice, profile.getId());

			assertThat(savedNotice.getTitle()).isEqualTo(title);
			assertThat(savedNotice.getBody()).isEqualTo(body);
			assertThat(savedNotice.getWriter()).isEqualTo(profile);

			return savedNotice;
		}catch (Exception e){
			throw new NoticeProjectException(e.getMessage());
		}
	}

}
