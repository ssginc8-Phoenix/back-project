package com.ssginc8.docto.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.user.entity.User;

/**
 * 유저 엔티티 전용 JPA 레포지토리
 */
public interface UserRepo extends JpaRepository<User, Long> {
}
