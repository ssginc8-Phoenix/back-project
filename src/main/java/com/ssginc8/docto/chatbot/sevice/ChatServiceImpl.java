package com.ssginc8.docto.chatbot.sevice;


import com.ssginc8.docto.chatbot.dto.ChatSymptomResponseDTO;
import com.ssginc8.docto.chatbot.provider.ChatProvider;
import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.hospital.dto.HospitalRecommendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

	private final ChatProvider chatProvider;
	private final ChatClient chatClient;

	// 증상 기반 → 진료과 추천
	@Override
	public ChatSymptomResponseDTO classifySymptom(String symptom) {
		String prompt = """
            당신은 병원 접수 상담원입니다.
            아래 증상을 보고 진료과를 **영어 키워드** 하나로만 답해주세요
            (CARDIOLOGY, NEUROLOGY, DERMATOLOGY, PEDIATRICS, RADIOLOGY,
             ONCOLOGY, GYNECOLOGY, PSYCHIATRY, GENERAL_SURGERY, UROLOGY,
             OPHTHALMOLOGY, ENT, INTERNAL_MEDICINE 중 하나)

            증상: "%s"
            """.formatted(symptom);

		String key = chatClient
			.prompt().user(prompt).call().content()
			.trim().toUpperCase();

		Specialization spec;
		try {
			spec = Specialization.valueOf(key);
		} catch (IllegalArgumentException e) {
			spec = Specialization.INTERNAL_MEDICINE;
		}

		String message = "%s(을)를 방문해 보세요!".formatted(spec.getDescription());
		return new ChatSymptomResponseDTO(spec, message);
	}

	// 진료과 기준 병원 추천
	@Override
	@Transactional(readOnly = true)
	public List<HospitalRecommendDTO> recommend(Specialization spec) {
		return chatProvider.fetchHospitals(spec).stream()
			.limit(3)
			.map(HospitalRecommendDTO::from)
			.toList();
	}

	// 증상 기반 병원 추천
	@Transactional(readOnly = true)
	public List<HospitalRecommendDTO> recommend(String symptom) {
		Specialization spec = classifySymptom(symptom).getSpecialization();
		return recommend(spec);
	}


}
