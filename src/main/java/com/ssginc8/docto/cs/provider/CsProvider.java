package com.ssginc8.docto.cs.provider;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.cs.entity.CsMessage;
import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.repo.CsMessageRepo;
import com.ssginc8.docto.cs.repo.CsRoomRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsProvider {

	private final CsRoomRepo csRoomRepo;
	private final CsMessageRepo csMessageRepo;

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
}
