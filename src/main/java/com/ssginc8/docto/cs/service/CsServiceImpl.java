package com.ssginc8.docto.cs.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.cs.dto.CsMessageRequest;
import com.ssginc8.docto.cs.dto.CsMessageResponse;
import com.ssginc8.docto.cs.dto.CsRoomCreateRequest;
import com.ssginc8.docto.cs.dto.CsRoomResponse;
import com.ssginc8.docto.cs.entity.CsMessage;
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
	@Transactional
	@Override
	public Page<CsRoomResponse> findAll(Pageable pageable) {
		return csProvider.findAll(pageable).map(CsRoomResponse::fromEntity);
	}

	// ✅ CS 채팅방 단건 조회
	@Transactional
	@Override
	public CsRoomResponse findById(Long csRoomId) {
		return CsRoomResponse.fromEntity(csProvider.findById(csRoomId));
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

		// 카카오톡 처럼 최신 메시지가 아래에 오도록 역순 정렬
		Collections.reverse(messages);

		return messages.stream()
			.map(CsMessageResponse::from)
			.collect(Collectors.toList());
	}

	@Override
	public void createMessage(Long csRoomId, CsMessageRequest request) {
		User user = userProvider.getUserById(request.getUserId());

		KafkaCsMessage messageDto = KafkaCsMessage.builder()
			.csRoomId(csRoomId)
			.userId(user.getUserId())
			.content(request.getContent())
			.createdAt(LocalDateTime.now())
			.messageType("TEXT")
			.build();

		kafkaProducerService.sendMessage(messageDto);
	}
}
