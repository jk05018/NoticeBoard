package com.example.notice.domain.notice.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.notice.domain.base.BaseIdEntity;
import com.example.notice.domain.profile.entity.Profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 공지사항 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notice extends BaseIdEntity {

	/* 제목 */
	@Embedded
	private Title title;

	/* 내용 */
	@Embedded
	private Body body;

	/* 작성 일자 */
	@CreatedDate
	private LocalDateTime createdDate;

	/* 마지막 수정 일자 */
	@LastModifiedDate
	private LocalDateTime updatedDate;

	/* 작성자 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Profile writer;

	/* 좋아요 한 사람 */
	@Embedded
	private Likes likes;

	private Notice(final Title title, final Body body, final Profile writer) {
		Optional.ofNullable(title).ifPresent(safeTitle -> this.title = safeTitle);
		Optional.ofNullable(body).ifPresent(safeBody -> this.body = safeBody);
		Optional.ofNullable(writer).ifPresent(safeWriter -> this.writer = safeWriter);
	}

	public static Notice of(final Title title, final Body body, final Profile writer) {
		return new Notice(title, body, writer);
	}

	public void updateNotice(final Title title, final Body body) {
		Optional.ofNullable(title).ifPresent(safeTitle -> this.title = safeTitle);
		Optional.ofNullable(body).ifPresent(safeBody -> this.body = safeBody);
	}

}
