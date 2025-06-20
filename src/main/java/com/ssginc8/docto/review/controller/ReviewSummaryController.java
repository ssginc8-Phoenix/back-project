package com.ssginc8.docto.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssginc8.docto.review.dto.ReviewSummaryResponse;
import com.ssginc8.docto.review.service.ReviewSummaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hospitals")
@RequiredArgsConstructor
public class ReviewSummaryController {

	private final ReviewSummaryService service;

	@GetMapping("/{hospitalId}/reviews/summary")
	public ResponseEntity<ReviewSummaryResponse> getSummary(@PathVariable Long hospitalId) {
		return ResponseEntity.ok(service.summarize(hospitalId));
	}
}
