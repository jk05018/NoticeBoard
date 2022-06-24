package com.example.notice.domain.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.notice.domain.BasicServiceTest;
import com.example.notice.domain.notice.entity.Body;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.notice.entity.Title;
import com.example.notice.domain.user.entity.Age;
import com.example.notice.domain.user.entity.Email;
import com.example.notice.domain.user.entity.NickName;
import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.exception.NoSuchNoticeException;
import com.example.notice.exception.NoticeProjectException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NoticeServiceTest extends BasicServiceTest {

  @Autowired
  private NoticeService noticeService;

  @Test
  void 공지사항을_ID로_조회할_수_있다() {
    // given
    final Title title = new Title("title");
    final Body body = new Body("body");
    final Slug slug = new Slug(title);

    final Notice notice = 공지사항_등록(title, body, testUser, testProfile);

    // when
    final Notice findNotice = noticeService.getBySlug(slug);

    // then
    assertThat(findNotice).extracting(Notice::getTitle, Notice::getSlug, Notice::getBody,
            Notice::getWriter)
        .isEqualTo(List.of(notice.getTitle(), notice.getSlug(), notice.getBody(), testProfile));

  }

  @Test
  void 공지사항_전체_목록을_조회할수_있다() {
    // given
    final Notice notice1 = 공지사항_등록(new Title("title1"), new Body("body1"), testUser, testProfile);
    final Notice notice2 = 공지사항_등록(new Title("title2"), new Body("body2"), testUser, testProfile);
    final Notice notice3 = 공지사항_등록(new Title("title3"), new Body("body3"), testUser, testProfile);

    // when
    final List<Notice> noticeList = noticeService.getList();

    // then
    assertThat(noticeList).containsExactlyInAnyOrder(notice1, notice2, notice3);
  }

  @Test
  void 공지사항의_제목과_내용을_수정할_수_있다() {
    // given
    final Title title = new Title("title");
    final Body body = new Body("body");

    final Notice notice = 공지사항_등록(title, body, testUser, testProfile);

    // when
    final Title updateTitle = new Title("update title");
    final Body updateBody = new Body("update Body");

    noticeService.updateNotice(Notice.updateOf(updateTitle, updateBody), new Slug(title));

    // then
    final Notice updatedNotice = noticeService.getBySlug(new Slug(title));

    assertThat(updatedNotice).extracting(Notice::getTitle, Notice::getBody)
        .isEqualTo(List.of(updateTitle, updateBody));

  }

  @Test
  void 공지사항을_ID로_삭제할수_있다() {
    // given
    final Title title = new Title("title");
    final Body body = new Body("body");
    final Slug slug = new Slug(title);

    final Notice notice = 공지사항_등록(title, body, testUser, testProfile);

    // when
    noticeService.deleteBySlug(slug);

    // then
    assertThrows(NoSuchNoticeException.class, () -> noticeService.getBySlug(slug));

  }

  @Test
  void 사용자가_작성한_공지사항을_조회할수_있다() {
    // given
    공지사항_랜덤_등록(10, "title", "body");

    // when
    final List<Notice> noticeListOfProfile = noticeService.getListByUsername(
        testUser.getUsername());

    // then
    assertThat(noticeListOfProfile.size()).isEqualTo(5);
    assertThat(noticeListOfProfile).extracting(Notice::getWriter)
        .allSatisfy(writer -> writer.equals(testProfile));
  }

  Notice 공지사항_등록(final Title title, final Body body, final User user, final Profile profile) {
    try {
      final Notice notice = Notice.createOf(title, body);

      final Notice savedNotice = noticeService.createNotice(notice, user.getUsername());

      assertThat(savedNotice).extracting(Notice::getTitle, Notice::getBody, Notice::getWriter)
          .isEqualTo(List.of(title, body, profile));

      return savedNotice;
    } catch (Exception e) {
      throw new NoticeProjectException("테스트 공지사항 등록 실패 입니다.", e);
    }
  }

  void 공지사항_랜덤_등록(final int noticecount, final String title, final String body) {
    try {
      final User testUser2 = 유저_등록(new Username("seunghan2"), new Email("seunghan2@naver.com"),
          new Password("pass123"));
      final Profile testProfile2 = 프로필_등록(testUser2, new NickName("testNickname2"), new Age(23));

      IntStream.range(0, noticecount)
          .forEach(count ->
              공지사항_등록(new Title(title + count), new Body(body + count),
                  (count % 2) == 0 ? testUser : testUser2,
                  (count % 2 == 0) ? testProfile : testProfile2)
          );
    } catch (Exception e) {
      throw new NoticeProjectException("테스트 공지사항 랜덤 등록 실패 입니다.", e);
    }
  }

}
