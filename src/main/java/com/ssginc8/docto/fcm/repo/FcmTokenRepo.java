package com.ssginc8.docto.fcm.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssginc8.docto.fcm.entity.FcmToken;

public interface FcmTokenRepo extends JpaRepository<FcmToken, Long> {

	Optional<FcmToken> findByToken(String token);

	@Query("SELECT f FROM FcmToken f WHERE f.user.userId = :userId ORDER BY f.updatedAt DESC")
	Optional<FcmToken> findLatestTokenByUserId(@Param("userId") Long userId);
}
