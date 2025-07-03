package com.ssginc8.docto.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.cs.dto.CsMessageResponse;
import com.ssginc8.docto.cs.entity.CsMessage;
import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.entity.Status;
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
	private final SimpMessageSendingOperations messagingTemplate;

	public void sendMessage(KafkaCsMessage messageDto) {
		kafkaTemplate.send(TOPIC_NAME, String.valueOf(messageDto.getCsRoomId()), messageDto)
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

			CsMessageResponse response = CsMessageResponse.from(message);

			// 1. 메세지를 보낸 사람이 고객이고, 상담사가 배정되어 있다면 상담사에게 실시간 전송
			if (messageDto.getUserId().equals(csRoom.getCustomerId())) {
				if (csRoom.getStatus() == Status.OPEN && csRoom.getAgentId() != null) {
					messagingTemplate.convertAndSendToUser(
						String.valueOf(csRoom.getAgentId()),
						"/queue/messages",
						response
					);
					log.info("고객 메시지 -> 상담사 실시간 전송 완료: agentId = {}", csRoom.getAgentId());
				} else {
					// 상담사 배정 전(=WAITING 상태) 일 경우, DB에만 저장하고 실시간 푸시는 하지 않음
					log.info("상담사 배정 대기 중인 고객 메세지. DB에만 저장됨: csRoomId = {}", csRoom.getCsRoomId());
				}
			}
			// 2. 메시지를 보낸 사람이 상담사라면, 고객에게 실시간 푸시 (항상 가능)
			else if (messageDto.getUserId().equals(csRoom.getAgentId())) {
				messagingTemplate.convertAndSendToUser(
					String.valueOf(csRoom.getCustomerId()),
					"/queue/messages",
					response
				);
				log.info("상담사 메시지 -> 고객 실시간 전송 완료: customerId = {}", csRoom.getCustomerId());
			}

		} catch (Exception e) {
			log.error("DB에 메세지 저장 및 WebSocket 전송 중 에러 발생: {}", e.getMessage(), e);
		}
	}
}
