package com.example.notice.domain.notice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.notice.domain.BasicServiceTest;
import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.notice.repository.NoticeRepository;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.exception.NoSuchNoticeException;
import com.example.notice.exception.NoticeProjectException;

class NoticeServiceTest extends BasicServiceTest {

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private NoticeRepository noticeRepository;

	@Test
	void 공지사항을_ID로_조회할_수_있다() {
		// given
		final Title title = new Title("title");
		final Body body = new Body("body");

		final Notice notice = 공지사항_등록(title, body, profile);

		// when
		final Notice findNotice = noticeService.getNoticeById(notice.getId());

		// then
		assertThat(findNotice).extracting(Notice::getTitle, Notice::getBody, Notice::getWriter)
			.isEqualTo(List.of(notice.getTitle(), notice.getBody(), profile));

	}

	@Test
	void 공지사항_전체_목록을_조회할수_있다() {
		// given
		final Notice notice1 = 공지사항_등록(new Title("title1"), new Body("body1"), profile);
		final Notice notice2 = 공지사항_등록(new Title("title2"), new Body("body2"), profile);
		final Notice notice3 = 공지사항_등록(new Title("title3"), new Body("body3"),profile);

		// when
		final List<Notice> noticeList = noticeService.getNoticeList();

		// then
		assertThat(noticeList).containsExactlyInAnyOrder(notice1, notice2, notice3);
	}

	@Test
	void 공지사항의_제목과_내용을_수정할_수_있다() {
		// given
		final Title title = new Title("title");
		final Body body = new Body("body");

		final Notice notice = 공지사항_등록(title, body,profile);

		// when
		final Title updateTitle = new Title("update title");
		final Body updateBody = new Body("update Body");

		noticeService.updateNotice(Notice.updateOf(updateTitle, updateBody), notice.getId());

		// then
		final Notice updatedNotice = noticeService.getNoticeById(notice.getId());

		assertThat(updatedNotice).extracting(Notice::getTitle, Notice::getBody)
			.isEqualTo(List.of(updateTitle, updateBody));

	}

	@Test
	void 공지사항을_ID로_삭제할수_있다() {
		// given
		final Notice notice = 공지사항_등록(new Title("title"), new Body("body"), profile);

		// when
		noticeService.deleteNoticeById(notice.getId());

		// then
		assertThrows(NoSuchNoticeException.class, () -> noticeService.getNoticeById(notice.getId()));

	}

	@Test
	void 사용자가_작성한_공지사항을_조회할수_있다() {
		// given
		공지사항_랜덤_등록(10, "title", "body");

		// when
		final List<Notice> noticeListOfProfile = noticeService.getNoticeListByProfile(profile.getId());

		// then
		assertThat(noticeListOfProfile.size()).isEqualTo(5);
		assertThat(noticeListOfProfile).extracting(Notice::getWriter)
			.allSatisfy(writer -> writer.equals(profile));
	}

	Notice 공지사항_등록(final Title title, final Body body, final Profile profile) {
		try {
			final Notice notice = Notice.createOf(title, body);

			final Notice savedNotice = noticeService.createNotice(notice, profile.getId());

			assertThat(savedNotice).extracting(Notice::getTitle, Notice::getBody, Notice::getWriter)
				.isEqualTo(List.of(title, body, profile));

			return savedNotice;
		} catch (Exception e) {
			throw new NoticeProjectException(e.getMessage());
		}
	}

	List<Notice> 공지사항_랜덤_등록(final int noticecount, final String title, final String body) {
		try {

			return IntStream.range(0, noticecount)
				.mapToObj(count ->
					공지사항_등록(new Title(title + count), new Body(body + count), ((count % 2) == 0) ? profile : profile2)
				)
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new NoticeProjectException(e.getMessage());
		}
	}

}
