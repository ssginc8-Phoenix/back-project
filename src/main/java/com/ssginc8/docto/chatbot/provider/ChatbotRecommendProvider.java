package com.ssginc8.docto.chatbot.provider;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.chatbot.repo.ChatRecommendRepo;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.chatbot.dto.HospitalSummary;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatbotRecommendProvider {

	private final ChatRecommendRepo chatRecommendRepo;

	@Transactional(readOnly = true)
	public List<HospitalSummary> findHospitalsByKeyword(String keyword) {
		List<Hospital> hospitals = chatRecommendRepo.findTop3ByNameContaining(keyword);

		return hospitals.stream()
			.map(HospitalSummary::from)
			.toList();
	}
}
