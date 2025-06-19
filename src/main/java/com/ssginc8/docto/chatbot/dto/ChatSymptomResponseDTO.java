package com.ssginc8.docto.chatbot.dto;

import com.ssginc8.docto.doctor.entity.Specialization;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatSymptomResponseDTO {
	private Specialization specialization;
	private String message;
}
