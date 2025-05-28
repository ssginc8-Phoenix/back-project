package com.ssginc8.docto.qna.controller;


import static com.ssginc8.docto.appointment.entity.QAppointment.*;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.repo.AppointmentRepo;
import com.ssginc8.docto.qna.dto.QaPostCreateRequest;
import com.ssginc8.docto.qna.dto.QaPostResponse;
import com.ssginc8.docto.qna.dto.QaPostUpdateRequest;
import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.service.QaPostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qnas")
@RequiredArgsConstructor
public class QnAController {

	private final QaPostService qaPostService;
	private final AppointmentRepo appointmentRepo;

	// 게시글 작성
	@PostMapping
	public ResponseEntity<QaPostResponse> createQaPost(
		@RequestBody @Valid QaPostCreateRequest request
	) {Appointment appointment = appointmentRepo.getReferenceById(request.getAppointmentId());
		QaPost saved = qaPostService.createQaPost(appointment, request.getContent());

		return ResponseEntity.ok(QaPostResponse.fromEntity(saved));
	}


	// 게시글 수정
	@PatchMapping("/{qnaId}")
	public ResponseEntity<QaPostResponse> updateQaPost(
		@PathVariable Long qnaId,
		@RequestBody @Valid QaPostUpdateRequest request
	) {QaPostResponse response = qaPostService.updateQaPost(qnaId, request.getContent());
		return ResponseEntity.ok(response);
	}

	// 게시글 삭제
	@DeleteMapping("/{qnaId}")
	public ResponseEntity<Void> deleteQaPost(@PathVariable Long qnaId) {
		qaPostService.deleteQaPost(qnaId);
		return ResponseEntity.noContent().build();
	}

	// 게시글 상세 조회
	@GetMapping("/{qnaId}")
	public ResponseEntity<QaPostResponse> getQaPost(@PathVariable Long qnaId) {
		QaPostResponse response = qaPostService.getQaPost(qnaId);
		return ResponseEntity.ok(response);
	}
}
