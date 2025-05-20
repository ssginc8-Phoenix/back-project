package com.ssginc8.docto.user.provider;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssginc8.docto.global.error.exception.userException.UserNotFoundException;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProvider {
	private final UserRepository userRepository;

	// 이메일 중복 검사
	public Optional<User> checkEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User loadUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}
}
