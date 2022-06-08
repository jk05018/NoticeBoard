package com.example.notice.domain.notice.service;

import java.util.List;

import com.example.notice.domain.notice.entity.Notice;

public interface NoticeService {

	Notice createNotice(final Notice notice, final Long profileId);

	List<Notice> getNoticeList();

	Notice getNoticeById(final Long noticeId);

	Notice updateNotice(final Notice notice);

	void deleteNoticeById(final Long noticeId);

	/* 사용자가 작성한 공지사항 목록 조회 */
	List<Notice> getNoticeListByProfile(final Long profileId);

	/* 사용자가 좋아요한 공지사항 목록 조회 */
	List<Notice> getLikedNoticeListByProfile(final Long profileId);







}
