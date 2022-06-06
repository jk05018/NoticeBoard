package com.example.notice.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notice.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
