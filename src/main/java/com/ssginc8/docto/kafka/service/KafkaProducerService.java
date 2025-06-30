package com.ssginc8.docto.kafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ssginc8.docto.kafka.dto.KafkaCsMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducerService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	// Kafka 토픽 이름
	private static final String TOPIC_NAME = "cs-chat-messages";

	public void sendMessage(KafkaCsMessage messageDto) {
		kafkaTemplate.send(TOPIC_NAME, String.valueOf(messageDto.getCsRoomId()),  messageDto)
			.whenComplete((result, ex) -> {
				if (ex == null) {
					log.info("메세지 전송 성공. Offset: {}", result.getRecordMetadata().offset());
				} else {
					log.error("메세지 전송 실패: {}", ex.getMessage());
				}
			})
		;
	}
}
