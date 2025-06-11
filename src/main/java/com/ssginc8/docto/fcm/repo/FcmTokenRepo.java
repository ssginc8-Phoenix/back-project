package com.ssginc8.docto.fcm.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.fcm.entity.FcmToken;

public interface FcmTokenRepo extends JpaRepository<FcmToken, Long> {

	Optional<FcmToken> findByToken(String token);

	Optional<FcmToken> findTopByUser_UserIdOrderByUpdatedAtDesc(Long userId);
}
