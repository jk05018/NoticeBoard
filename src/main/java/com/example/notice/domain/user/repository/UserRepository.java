package com.example.notice.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;

public interface UserRepository extends JpaRepository<User, Long> {

	@EntityGraph(attributePaths = {"profile"})
	Optional<User> findByUsername(final Username username);
}
