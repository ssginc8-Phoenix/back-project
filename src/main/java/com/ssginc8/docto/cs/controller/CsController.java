package com.ssginc8.docto.cs.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.cs.dto.AssignAgentRequest;
import com.ssginc8.docto.cs.dto.CsMessageResponse;
import com.ssginc8.docto.cs.dto.CsRoomCreateRequest;
import com.ssginc8.docto.cs.dto.CsRoomResponse;
import com.ssginc8.docto.cs.service.CsService;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class CsController {

	private final CsService csService;
	private final UserService userService;
	private final SimpMessageSendingOperations messagingTemplate;    // 메세지 브로커로 메세지 전송

	/*  회원별 CS 조회
	 *	URL: /api/v1/csrooms/users/me
	 *	Method: GET
	 */

	/* ✅ 관리자용 CS 채팅방 리스트 조회
	 *	URL: /api/v1/admin/csrooms
	 *	Method: GET
	 */
	@GetMapping("/admin/csrooms")
	public ResponseEntity<Page<CsRoomResponse>> getCsRoomList(Pageable pageable) {

		return ResponseEntity.ok(csService.findAll(pageable));
	}

	/* ✅ CS 채팅방 단건(Detail) 조회
	 *	URL: /api/v1/csrooms/{csRoomId}
	 *	Method: GET
	 */
	@GetMapping("/csRooms/{csRoomId}")
	public ResponseEntity<CsRoomResponse> getCsRoomDetail(@PathVariable Long csRoomId) {
		return ResponseEntity.ok(csService.findById(csRoomId));
	}

	/* ✅ CS 채팅방 생성
	 *	URL: /api/v1/csrooms
	 *	Method: POST
	 */
	@PostMapping("/csrooms")
	public ResponseEntity<Long> createCsRoom(@RequestBody CsRoomCreateRequest request) {
		Long csRoomId = csService.createCsRoom(request);
		return ResponseEntity.ok(csRoomId);
	}

	/* ✅ CS 채팅방 상담사 배정
	 *	URL: /api/v1/csrooms/{csRoomId}/assign"
	 *	Method: PATCH
	 */
	@PatchMapping("/csrooms/{csRoomId}/assign")
	public ResponseEntity<Void> assignAgent(@PathVariable Long csRoomId, @RequestBody AssignAgentRequest request) {
		csService.assignAgent(csRoomId, request.getAgentId());

		return ResponseEntity.noContent().build();
	}

	/* ✅ 채팅방 상담 상태 변경
	 *	URL: /api/v1/csrooms/{csRoomId}/status
	 *	Method: PATCH
	 */
	@PatchMapping("/csrooms/{csRoomId}/status")
	public ResponseEntity<Void> updateCsRoomStatus(
		@PathVariable Long csRoomId,
		@RequestBody String status) {
		csService.updateCsRoomStatus(csRoomId, status);

		return ResponseEntity.noContent().build();
	}

	/* ✅ CS 채팅방 삭제
	 *	URL: /api/v1/csrooms/{csRoomId}
	 *	Method: DELETE
	 */
	@DeleteMapping("/csrooms/{csRoomId}")
	public ResponseEntity<Void> deleteCsRoom(@PathVariable Long csRoomId) {
		csService.deleteCsRoom(csRoomId);

		return ResponseEntity.noContent().build();
	}

	/* 채팅방 -> 메시지 리스트 조회 (무한 스크롤)
	 *	URL: /api/v1/csrooms/{csRoomId}/messages?before=2024-05-20T12:00:00&size=20
	 *	Method: GET
	 */
	@GetMapping("/csrooms/{csRoomId}/messages")
	public ResponseEntity<List<CsMessageResponse>> getMessages(
		@PathVariable Long csRoomId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime before,
		@RequestParam(defaultValue = "20") int size) {

		List<CsMessageResponse> messages = csService.getMessages(csRoomId, before, size);

		return ResponseEntity.ok(messages);
	}
}
