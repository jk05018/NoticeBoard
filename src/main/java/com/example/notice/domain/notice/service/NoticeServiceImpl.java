package com.example.notice.domain.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.notice.repository.NoticeRepository;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.domain.user.repository.UserRepository;
import com.example.notice.exception.NoSuchNoticeException;
import com.example.notice.exception.NoSuchProfileException;
import com.example.notice.exception.NoSuchUserException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRepository;
	private final UserRepository userRepository;

	@Override
	public Notice createNotice(final Notice notice, final Username username) {
		final User user = userRepository.findByUsername(username)
			.orElseThrow(NoSuchProfileException::new);

		notice.assignWriter(user.getProfile());

		return noticeRepository.save(notice);
	}

	@Override
	public List<Notice> getNoticeList() {
		return noticeRepository.findAll();
	}

	@Override
	public Notice getNoticeBySlug(Slug slug) {
		return noticeRepository.findBySlug(slug)
			.orElseThrow(NoSuchNoticeException::new);
	}

	@Override
	public Notice updateNotice(final Notice updateNotice, final Slug slug) {
		Notice notice = noticeRepository.findBySlug(slug)
			.orElseThrow(NoSuchNoticeException::new);

		notice.updateNotice(updateNotice);

		return notice;
	}

	@Override
	public void deleteNoticeBySlug(final Slug slug) {
		noticeRepository.deleteBySlug(slug);
	}

	@Override
	public List<Notice> getNoticeListByUsername(final Username username) {
		final Profile writer = userRepository.findByUsername(username)
			.orElseThrow(NoSuchUserException::new).getProfile();

		return noticeRepository.findAllByWriter(writer);
	}

	@Override
	public List<Notice> getLikedNoticeListByProfile(final Username username) {
		return null;
	}
}
