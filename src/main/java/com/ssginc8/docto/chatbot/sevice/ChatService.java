package com.ssginc8.docto.chatbot.sevice;


import com.ssginc8.docto.chatbot.dto.ChatSymptomResponseDTO;
import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.hospital.dto.HospitalRecommendDTO;

import java.util.List;

public interface ChatService {

	ChatSymptomResponseDTO classifySymptom(String symptom);

	List<HospitalRecommendDTO> recommend(Specialization specialization);

}
