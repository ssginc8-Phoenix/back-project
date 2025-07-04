package com.ssginc8.docto.chatbot.repo;

import com.ssginc8.docto.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRecommendRepo extends JpaRepository<Hospital, Long> {
	List<Hospital> findTop3ByNameContaining(String keyword);
}
