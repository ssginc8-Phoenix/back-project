package com.ssginc8.docto.cs.controller;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.ssginc8.docto.cs.dto.CsMessageRequest;
import com.ssginc8.docto.cs.dto.CsMessageResponse;
import com.ssginc8.docto.cs.service.CsService;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketChatController {

	private final CsService csService;
	private final UserService userService;
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 클라이언트가 /app/chat.sendMessage 로 메세지를 보낼 때
	 */
	@MessageMapping("/chat.sendMessage")
	public void sendMessage(
		@Payload CsMessageRequest chatMessage,
		Principal principal
	) {
		// 1) 로그인 사용자 조회
		String userUuid = principal.getName();
		User user = userService.findByUuid(userUuid);
		Long csRoomId = chatMessage.getCsRoomId();
		if (csRoomId == null) {
			log.error("CsRoomId is null in CsMessageRequest for user {}", user.getUserId());
			return;
		}
		// 3) DB에 메시지 저장하고, 응답 DTO를 받음
		CsMessageResponse saved = csService.createMessage(
			csRoomId,
			user.getUserId(),
			chatMessage.getContent()
		);

		log.info("Message saved for csRoomId: {}, userId: {}, content: {}",
			saved.getCsRoomId(), saved.getUserId(), saved.getContent());
		// 4) system=true 로 새 인스턴스 생성
		CsMessageResponse withSystem = saved.toBuilder()
			.system(chatMessage.isSystem())
			.build();

		// 4) 해당 룸을 구독 중인 클라이언트에게 브로드캐스트
		messagingTemplate.convertAndSend(
			"/topic/rooms/" + csRoomId,
			withSystem
		);

	}

	/**
	 * WebSocket 클라이언트 연결 시 발생하는 이벤트 처리
	 */
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		log.info("WebSocket Session 연결됨: sessionId={}, user={}",
			headerAccessor.getSessionId(), headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : "anonymous");

		if (headerAccessor.getUser() != null && headerAccessor.getUser().getName() != null) {
			headerAccessor.getSessionAttributes().put("principalName", headerAccessor.getUser().getName());
			log.info("Stored principalName {} in WebSocket session attributes.", headerAccessor.getUser().getName());
		}
	}
}
