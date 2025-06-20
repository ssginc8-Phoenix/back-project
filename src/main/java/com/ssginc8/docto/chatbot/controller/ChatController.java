package com.ssginc8.docto.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.chatbot.dto.ChatRequestDTO;
import com.ssginc8.docto.chatbot.dto.ChatSymptomResponseDTO;
import com.ssginc8.docto.chatbot.sevice.ChatService;
import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.hospital.dto.HospitalRecommendDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@PostMapping("/symptom")
	public ChatSymptomResponseDTO classifySymptom(@RequestBody ChatRequestDTO dto) {
		return chatService.classifySymptom(dto.getSymptom());
	}

	@GetMapping("/recommend")
	public List<HospitalRecommendDTO> recommend(
		@RequestParam Specialization specialization){
		return chatService.recommend(specialization);
	}
}
