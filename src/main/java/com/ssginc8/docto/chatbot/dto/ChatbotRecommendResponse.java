package com.ssginc8.docto.chatbot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatbotRecommendResponse {
	private String symptom;
	private String specialization;
	private List<String> hospitalNames;
}

