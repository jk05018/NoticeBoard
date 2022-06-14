package com.example.notice.domain.notice.service;

import java.util.List;

import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.user.entity.Username;

public interface NoticeService {

	Notice createNotice(final Notice notice, final Username username);

	List<Notice> getNoticeList();

	Notice getNoticeBySlug(final Slug slug);

	Notice updateNotice(final Notice udpateNotice, final Slug slug);

	void deleteNoticeBySlug(final Slug slug);

	/* 사용자가 작성한 공지사항 목록 조회 */
	List<Notice> getNoticeListByUsername(final Username username);

	/* 사용자가 좋아요한 공지사항 목록 조회 */
	List<Notice> getLikedNoticeListByProfile(final Username username);







}
