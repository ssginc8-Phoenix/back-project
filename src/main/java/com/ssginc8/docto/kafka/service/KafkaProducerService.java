package com.ssginc8.docto.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.cs.entity.CsMessage;
import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.provider.CsProvider;
import com.ssginc8.docto.kafka.dto.KafkaCsMessage;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducerService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	// Kafka 토픽 이름
	private static final String TOPIC_NAME = "cs-chat-messages";
	private final CsProvider csProvider;
	private final UserProvider userProvider;

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

	@KafkaListener(topics = "cs-chat-messages", groupId = "cs-message-group", containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void consumeMessage(KafkaCsMessage messageDto) {
		log.info("카프카에서 전송된 메세지: {}", messageDto);

		try {
			CsRoom csRoom = csProvider.findById(messageDto.getCsRoomId());
			User user = userProvider.getUserById(messageDto.getUserId());

			CsMessage message = CsMessage.create(
				csRoom,
				messageDto.getUserId(),
				messageDto.getContent()
			);

			csProvider.save(message);
			log.info("DB에 메세지 저장됨: {}", message.getCsMessageId());
		} catch (Exception e) {
			log.error("DB에 메세지 저장 중 에러 발생: {}", e.getMessage(), e);
		}
	}
}
