package com.ssginc8.docto.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long > {
	boolean existsByEmail(String email);
}
