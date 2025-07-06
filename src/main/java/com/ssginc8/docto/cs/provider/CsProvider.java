package com.ssginc8.docto.cs.provider;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.cs.entity.CsMessage;
import com.ssginc8.docto.cs.entity.CsNote;
import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.repo.CsMessageRepo;
import com.ssginc8.docto.cs.repo.CsNoteRepo;
import com.ssginc8.docto.cs.repo.CsRoomRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsProvider {

	private final CsRoomRepo csRoomRepo;
	private final CsMessageRepo csMessageRepo;
	private final CsNoteRepo csNoteRepo;

	@Transactional(readOnly = true)
	public Page<CsRoom> findAll(Pageable pageable) {

		return csRoomRepo.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public CsRoom findById(Long csRoomId) {
		return csRoomRepo.findById(csRoomId).orElseThrow(
			() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다. id = " + csRoomId));
	}

	@Transactional
	public Long save(CsRoom csRoom) {
		return csRoomRepo.save(csRoom).getCsRoomId();
	}

	@Transactional(readOnly = true)
	public List<CsMessage> getMessagesBefore(Long csRoomId, LocalDateTime before, int size) {
		return csMessageRepo.findCsMessageBefore(
			csRoomId,
			before,
			PageRequest.of(0, size)
		);
	}

	@Transactional
	public Long save(CsMessage csMessage) {
		return csMessageRepo.save(csMessage).getCsMessageId();
	}



	/**
	 * 해당 고객이 참여한 모든 방의 ID 목록을 반환
	 */
	@Transactional(readOnly = true)
	public List<Long> getRoomIdsByCustomerId(Long customerId) {
		List<CsRoom> rooms = csRoomRepo.findAllByCustomerId(customerId);
		if (rooms == null || rooms.isEmpty()) {
			return Collections.emptyList();
		}
		return rooms.stream()
			.map(CsRoom::getCsRoomId)
			.collect(Collectors.toList());
	}

	/**
	 * 여러 방(roomIds)에 속한 메시지를 before 시점 이전부터
	 * createdAt 내림차순으로 pageable.size 만큼 조회
	 */
	@Transactional(readOnly = true)
	public List<CsMessage> findMessagesByRoomIdsBefore(
		List<Long> roomIds,
		LocalDateTime before,
		Pageable pageable  // PageRequest.of(0, size) 형태로 넘겨주세요
	) {
		if (roomIds == null || roomIds.isEmpty()) {
			return Collections.emptyList();
		}
		return csMessageRepo.findMessagesByRoomIdsBefore(
			roomIds,
			before != null ? before : LocalDateTime.now(),
			pageable
		);  // CsMessageRepo 에 아래 메서드가 있어야 합니다
	}

	@Transactional(readOnly = true)
	public CsMessage findMessageById(Long csMessageId) {
		return csMessageRepo.findById(csMessageId)
			.orElseThrow(() ->
				new IllegalArgumentException("해당 메시지가 존재하지 않습니다. id = " + csMessageId)
			);
	}

	/**
	 * CsRoom 조회
	 */
	public Optional<CsNote> findRoomById(Long csRoomId) {
		return csNoteRepo.findById(csRoomId);
	}

	/** CsNote 저장 */
	public CsNote saveNote(CsNote note) {
		return csNoteRepo.save(note);
	}

	/** 특정 방의 CsNote 페이징 조회 (최신순) */
	public Page<CsNote> findNotesByRoom(Long csRoomId, Pageable pageable) {
		return csNoteRepo
			.findByCsRoom_CsRoomIdOrderByCreatedAtDesc(csRoomId, pageable);
	}

	public CsNote save(CsNote note) {
		return csNoteRepo.save(note);
	}
}
