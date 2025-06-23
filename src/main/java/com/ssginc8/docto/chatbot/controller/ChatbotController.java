package com.ssginc8.docto.chatbot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.chatbot.dto.ChatbotClassifyRequest;
import com.ssginc8.docto.chatbot.dto.ChatbotClassifyResponse;
import com.ssginc8.docto.chatbot.dto.HospitalSummary;
import com.ssginc8.docto.chatbot.service.ChatbotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatbotController {

	private final ChatbotService chatbotService;

	@PostMapping("/chatbot/classify")
	public ResponseEntity<ChatbotClassifyResponse> classify(@RequestBody ChatbotClassifyRequest request) {
		String specialization = chatbotService.classifySpecialization(request.getSymptom());
		return ResponseEntity.ok(new ChatbotClassifyResponse(specialization));
	}

	@GetMapping("/chatbot/recommend")
	public ResponseEntity<List<HospitalSummary>> recommend(@RequestParam String specialization) {
		List<HospitalSummary> hospitals = chatbotService.recommendHospitals(specialization);
		return ResponseEntity.ok(hospitals);
	}

}
