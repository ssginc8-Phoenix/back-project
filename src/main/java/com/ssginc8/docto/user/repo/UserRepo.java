package com.ssginc8.docto.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.user.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
