package com.example.notice.domain.user.service;

import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;

public interface UserService {

	User login(final Username principal, final Password credentials);

	User registerUser(final User user);

	User findByUsername(final Username username);
}
