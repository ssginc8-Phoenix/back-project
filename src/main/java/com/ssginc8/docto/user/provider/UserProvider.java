package com.ssginc8.docto.user.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserProvider {

	private final UserRepo userRepo;

	@Transactional(readOnly = true)
	public User getUserById(Long userId) {
		return userRepo.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. id = " + userId)
		);
	}
}
