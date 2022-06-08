package com.example.notice.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notice.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
