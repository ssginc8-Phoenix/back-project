package com.ssginc8.docto.qna.controller;

import com.ssginc8.docto.qna.dto.CommentRequest;
import com.ssginc8.docto.qna.dto.CommentResponse;
import com.ssginc8.docto.qna.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/qnas")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	// 의사 답변 생성
	@PostMapping("/{qnaId}/comments")
	@PreAuthorize("hasRole('DOCTOR')")
	public ResponseEntity<CommentResponse> createComment(
		@PathVariable Long qnaId,
		@RequestBody @Valid CommentRequest request
	) {
		CommentResponse response = commentService.createComment(qnaId, request.getContent());
		return ResponseEntity.ok(response);
	}

	// 의사 답변 리스트 조회
	@GetMapping("/{qnaId}/comments")
	public ResponseEntity<List<CommentResponse>> getCommentsByPost(
		@PathVariable Long qnaId
	) {
		List<CommentResponse> responses = commentService.getCommentsByPost(qnaId);
		return ResponseEntity.ok(responses);
	}

	// 의사 답변 수정
	@PatchMapping("/comments/{commentId}")
	public ResponseEntity<CommentResponse> updateComment(
		@PathVariable Long commentId,
		@RequestBody @Valid CommentRequest request
	) {
		CommentResponse response = commentService.updateComment(commentId, request.getContent());
		return ResponseEntity.ok(response);
	}


	// 의사 단건 답변 조회
	@GetMapping("/comments/{commentId}")
	public ResponseEntity<CommentResponse> getComment(
		@PathVariable Long commentId
	) {
		CommentResponse response = commentService.getComment(commentId);
		return ResponseEntity.ok(response);
	}

	// 답변 삭제
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable Long commentId
	) {
		commentService.deleteComment(commentId);
		return ResponseEntity.noContent().build();
	}
}
