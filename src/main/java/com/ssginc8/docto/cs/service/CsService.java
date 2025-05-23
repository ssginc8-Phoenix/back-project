package com.ssginc8.docto.cs.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.cs.dto.CsMessageRequest;
import com.ssginc8.docto.cs.dto.CsMessageResponse;
import com.ssginc8.docto.cs.dto.CsRoomCreateRequest;
import com.ssginc8.docto.cs.dto.CsRoomResponse;

public interface CsService {

	// ✅ 관리자용 CS 채팅방 리스트 조회
	Page<CsRoomResponse> findAll(Pageable pageable);

	CsRoomResponse findById(Long csRoomId);

	Long createCsRoom(CsRoomCreateRequest request);

	void assignAgent(Long csRoomId, Long agentId);

	void updateCsRoomStatus(Long csRoomId, String status);

	void deleteCsRoom(Long csRoomId);


	List<CsMessageResponse> getMessages(Long csRoomId, LocalDateTime before, int size);

	Long createMessage(Long csRoomId, CsMessageRequest request);
}
