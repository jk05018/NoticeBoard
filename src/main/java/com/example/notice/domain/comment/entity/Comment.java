package com.example.notice.domain.comment.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.notice.domain.base.BaseIdEntity;
import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.user.entity.Profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 댓글 */
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseIdEntity {

	/* 내용 */
	@Embedded
	private Content content;

	/* 공지사항 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notice_id")
	private Notice notice;

	/* 작성자 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	private Profile writer;

	/* 댓글 작성 일자 */
	@CreatedDate
	private LocalDateTime createdDate;

	/* 댓글 최근 수정 일자 */
	@LastModifiedDate
	private LocalDateTime updatedDate;

	private Comment(final Content content) {
		Optional.ofNullable(content).ifPresent(safeContent -> this.content = safeContent);
	}

	public static Comment of(final Content content) {
		return new Comment(content);
	}

	public void assignNoticeAndWriter(final Notice notice, final Profile writer){
		Optional.ofNullable(notice).ifPresent(safeNotice -> this.notice = safeNotice);
		Optional.ofNullable(writer).ifPresent(safeWriter -> this.writer = safeWriter);
	}

	public void updateBody(final Content content) {
		Optional.ofNullable(content).ifPresent(safeContent -> this.content = safeContent);
	}

}
