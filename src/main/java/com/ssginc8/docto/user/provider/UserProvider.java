package com.ssginc8.docto.user.provider;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ssginc8.docto.global.error.exception.userException.EmailNotFoundException;
import com.ssginc8.docto.global.error.exception.userException.UserNotFoundException;
import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repository.UserRepo;
import com.ssginc8.docto.user.repository.UserSearchRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProvider {
	private final UserRepo userRepo;
	private final UserSearchRepo userSearchRepo;

	public User loadUserByEmailOrException(String email) {
		return userRepo.findByEmail(email)
			.orElseThrow(UserNotFoundException::new);
	}

	public Optional<User> loadUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User loadUserByUuid(String uuid) {
		return userRepo.findByUuid(uuid)
			.orElseThrow(UserNotFoundException::new);
	}

	public User loadUserByProviderId(String providerId) {
		return userRepo.findByProviderId(providerId)
			.orElseThrow(UserNotFoundException::new);
	}

	public User loadEmailByNameAndPhone(String name, String phone) {
		return userRepo.findByNameAndPhone(name, phone)
			.orElseThrow(EmailNotFoundException::new);
	}

	public Page<User> loadUsersByRole(Role role, Pageable pageable) {
		return userSearchRepo.findByRoleAndDeletedAtIsNotNull(role, pageable);
	}

	public User createUser(User user) {
		return userRepo.save(user);
	}
}
