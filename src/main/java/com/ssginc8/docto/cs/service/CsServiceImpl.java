package com.ssginc8.docto.cs.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.cs.dto.CsMessageRequest;
import com.ssginc8.docto.cs.dto.CsMessageResponse;
import com.ssginc8.docto.cs.dto.CsNoteRequest;
import com.ssginc8.docto.cs.dto.CsNoteResponse;
import com.ssginc8.docto.cs.dto.CsRoomCreateRequest;
import com.ssginc8.docto.cs.dto.CsRoomResponse;
import com.ssginc8.docto.cs.entity.CsMessage;
import com.ssginc8.docto.cs.entity.CsNote;
import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.entity.Status;
import com.ssginc8.docto.cs.provider.CsProvider;
import com.ssginc8.docto.kafka.dto.KafkaCsMessage;
import com.ssginc8.docto.kafka.service.KafkaProducerService;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsServiceImpl implements CsService {

	private final CsProvider csProvider;
	private final UserProvider userProvider;
	private final KafkaProducerService kafkaProducerService;

	// ✅ 관리자용 CS 채팅방 리스트 조회
	@Override
	@Transactional(readOnly = true)
	public Page<CsRoomResponse> findAll(Pageable pageable) {
		return csProvider.findAll(pageable).map(csRoom -> {
			User customer = userProvider.getUserById(csRoom.getCustomerId());
			return CsRoomResponse.fromEntity(
				csRoom,
				customer.getName(),
				customer.getProfileUrl()
			);
		});
	}

	// ✅ CS 채팅방 단건 조회
	@Transactional
	@Override
	public CsRoomResponse findById(Long csRoomId) {
		CsRoom room = csProvider.findById(csRoomId);
		User customer = userProvider.getUserById(room.getCustomerId());
		// 생성자 파라미터 순서: name, csRoomId, customerId, agentId, avatarUrl, status
		return new CsRoomResponse(
			customer.getName(),
			room.getCsRoomId(),
			room.getCustomerId(),
			room.getAgentId(),
			customer.getProfileUrl(),  // 여기에 avatar URL 추가
			room.getStatus()
		);
	}

	// ✅ CS 채팅방 생성
	@Override
	public Long createCsRoom(CsRoomCreateRequest request) {
		User user = userProvider.getUserById(request.getCustomerId());

		CsRoom csRoom = CsRoom.create(user.getUserId());
		return csProvider.save(csRoom);
	}

	// ✅ WAITING 중인 채팅방 상담사 배정
	@Transactional
	@Override
	public void assignAgent(Long csRoomId, Long agentId) {
		User user = userProvider.getUserById(agentId);

		CsRoom csRoom = csProvider.findById(csRoomId);
		csRoom.assignAgent(user.getUserId());
	}

	// ✅ 채팅방 상태 변경
	@Transactional
	@Override
	public void updateCsRoomStatus(Long csRoomId, String status) {
		CsRoom csRoom = csProvider.findById(csRoomId);
		Status newStatus = Status.from(status);
		csRoom.changeStatus(newStatus);

		csProvider.save(csRoom);
	}

	@Transactional
	@Override
	public void deleteCsRoom(Long csRoomId) {
		CsRoom csRoom = csProvider.findById(csRoomId);
		csRoom.delete();
	}

	@Transactional
	@Override
	public List<CsMessageResponse> getMessages(Long csRoomId, LocalDateTime before, int size) {
		if (before == null) {
			before = LocalDateTime.now();
		}

		List<CsMessage> messages = csProvider.getMessagesBefore(csRoomId, before, size);


		return messages.stream()
			.map(CsMessageResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public CsMessageResponse createMessage(
		Long csRoomId,
		Long userId,
		String content
	) {
		// 1) 방 검증
		CsRoom room = csProvider.findById(csRoomId);

		// 2) Kafka 이벤트 생성 & 발행 (DB 저장은 컨슈머에서)
		KafkaCsMessage messageDto = KafkaCsMessage.builder()
			.csRoomId(csRoomId)
			.userId(userId)
			.content(content)
			.createdAt(LocalDateTime.now())
			.messageType("TEXT")
			.build();
		kafkaProducerService.sendMessage(messageDto);

		// 3) DTO 반환 (ID는 컨슈머 저장 후에 알 수 있으니 null 처리)
		return new CsMessageResponse(
			null,
			messageDto.getUserId(),
			messageDto.getContent(),
			messageDto.getCreatedAt(),
			messageDto.getCsRoomId()
		);
	}

	@Override
	public List<CsMessageResponse> getMessagesByCustomer(
		Long customerId,
		LocalDateTime before,
		int size
	) {
		if (before == null) {
			before = LocalDateTime.now();
		}

		// 1) 고객이 속한 방 ID 목록 조회
		List<Long> roomIds = csProvider.getRoomIdsByCustomerId(customerId);
		if (roomIds.isEmpty()) {
			return Collections.emptyList();
		}

		// 2) 메시지 조회(PageRequest.of(0, size) 로 size 만큼 가져오기)
		var pageable = PageRequest.of(0, size);
		var messages = csProvider.findMessagesByRoomIdsBefore(roomIds, before, pageable);

		// 3) DTO로 변환
		return CsMessageResponse.fromEntities(messages);
	}

	@Override
	@Transactional
	public CsNoteResponse saveNote(Long csRoomId, CsNoteRequest request) {
		// ① ID로만 방 조회
		CsRoom csRoom = csProvider.findById(csRoomId);


		// ② 노트 엔티티 생성
		CsNote note = CsNote.create(csRoom, request.getAgentId(), request.getContent());

		// ③ 저장
		CsNote saved = csProvider.save(note);

		// ④ 생성된 노트를 DTO로 반환
		return CsNoteResponse.fromEntity(saved);
	}


	@Transactional(readOnly = true)
	public Page<CsNoteResponse> getNotes(Long csRoomId, Pageable pageable) {
		return csProvider.findNotesByRoom(csRoomId, pageable)
			.map(CsNoteResponse::fromEntity);
	}
}