package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

//전체 조회(관리자,상세리스트)용
@Data
public class ReviewListResponse {

	private Long reviewId;
	private Long appointmentId;

	private Long hospitalId;
	private String hospitalName;

	private Long doctorId;
	private String doctorName;

	private Long userId;
	private String userName;

	private String contents;
	private List<String> keywords;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


	}



