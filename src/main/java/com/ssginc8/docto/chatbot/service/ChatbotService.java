package com.ssginc8.docto.chatbot.service;

import java.util.List;

import com.ssginc8.docto.chatbot.dto.HospitalSummary;

public interface ChatbotService {
	String classifySpecialization(String symptom);

	List<HospitalSummary> recommendHospitals(String specialization);
}

