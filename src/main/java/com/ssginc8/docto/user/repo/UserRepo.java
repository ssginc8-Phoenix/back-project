package com.ssginc8.docto.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.user.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByUuid(String uuid);

	Optional<User> findByProviderId(String providerId);

	Optional<User> findByNameAndPhone(String name, String phone);
}
