package com.ssginc8.docto.chatbot.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.ssginc8.docto.chatbot.dto.HospitalSummary;
import com.ssginc8.docto.chatbot.provider.ChatbotRecommendProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

	private final ChatClient chatClient;
	private final ChatbotRecommendProvider provider;

	@Override
	public String classifySpecialization(String symptom) {
		String prompt = """
			다음 증상을 듣고 어떤 진료과를 가야 할지 한 단어로만 알려주세요.
			    CARDIOLOGY("심장내과"),
				NEUROLOGY("신경과"),
				DERMATOLOGY("피부과"),
				PEDIATRICS("소아과"),
				RADIOLOGY("영상의학과"),
				ONCOLOGY("종양내과"),
				GYNECOLOGY("산부인과"),
				PSYCHIATRY("정신과"),
				GENERAL_SURGERY("일반외과"),
				UROLOGY("비뇨기과"),
				OPHTHALMOLOGY("안과"),
				ENT("이비인후과"),
				INTERNAL_MEDICINE("내과"); 이 중에서 골라서 한국어로 말해주세요.
			---
			증상: %s
			""".formatted(symptom);

		return chatClient
			.prompt(prompt)
			.call()
			.content()
			.trim();
	}

	@Override
	public List<HospitalSummary> recommendHospitals(String specialization) {
		// 1. 내부 DB 조회
		List<HospitalSummary> list = provider.findHospitalsByKeyword(specialization);
		if (!list.isEmpty()) return list;

		// 2. GPT로 fallback 추천 요청
		String fallbackPrompt = "부산에서 유명한 " + specialization + " 병원 3곳만 추천해줘. 이름만 말해줘.";
		String gptResult = chatClient.prompt(fallbackPrompt).call().content().trim();

		// 3. GPT 응답을 줄 단위로 파싱
		String[] lines = gptResult.split("\n");

		return Arrays.stream(lines)
			.map(line -> {
				String name = line.replaceAll("^[0-9]+\\.\\s*", "").trim();
				return new HospitalSummary(null, name, "부산시", "정보 없음");
			})
			.collect(Collectors.toList());
	}
}
