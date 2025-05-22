package com.ssginc8.docto.user.provider;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssginc8.docto.global.error.exception.userException.UserNotFoundException;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProvider {
	private final UserRepo userRepo;

	// 이메일 중복 검사
	public Optional<User> checkEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User loadUserByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(UserNotFoundException::new);
	}

	public User loadUserByUuid(String uuid) {
		return userRepo.findByUuid(uuid).orElseThrow(UserNotFoundException::new);
	}

	public User loadUserByProviderId(String providerId) {
		return userRepo.findByProviderId(providerId)
			.orElseThrow(UserNotFoundException::new);
	}

	public User createUser(User user) {
		return userRepo.save(user);
	}
}
