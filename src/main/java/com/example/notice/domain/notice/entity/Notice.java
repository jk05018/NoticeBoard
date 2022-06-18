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
import com.example.notice.domain.user.entity.Profile;

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

	/* 식별자 */
	@Embedded
	private Slug slug;

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

	private Notice(final Title title, final Body body) {
		Optional.ofNullable(title).ifPresent(safeTitle -> {
			this.title = safeTitle;
			this.slug = new Slug(title);
		});
		Optional.ofNullable(body).ifPresent(safeBody -> this.body = safeBody);
	}

	public static Notice createOf(final Title title, final Body body) {
		return new Notice(title, body);
	}

	public static Notice updateOf(final Title title, final Body body){
		return new Notice(title, body);
	}

	public void assignWriter(final Profile writer){
		Optional.ofNullable(writer).ifPresent(safeWriter -> this.writer = safeWriter);
	}

	public void updateNotice(final Notice notice) {
		Optional.ofNullable(notice.title).ifPresent(safeTitle -> this.title = safeTitle);
		Optional.ofNullable(notice.body).ifPresent(safeBody -> this.body = safeBody);
	}

}
