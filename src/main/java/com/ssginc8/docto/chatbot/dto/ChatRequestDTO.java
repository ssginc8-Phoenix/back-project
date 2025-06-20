package com.ssginc8.docto.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDTO {
	@NotBlank(message = "증상을 입력하세요.")
	private String symptom;
}
