package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.review.entity.KeywordType;
import com.ssginc8.docto.review.entity.Review;
import lombok.Getter;

@Getter
public class ReviewMyListResponse {

	private Long reviewId;
	private String contents;
	private LocalDateTime createdAt;
	private List<String> keywords;

	private Long hospitalId;
	private String hospitalName;
	private Long doctorId;
	private String doctorName;

	public static ReviewMyListResponse fromEntity(Review reviews) {

		ReviewMyListResponse dto = new ReviewMyListResponse();
		dto.reviewId   = reviews.getReviewId();
		dto.contents   = reviews.getContents();
		dto.createdAt  = reviews.getCreatedAt();
		// Review 엔티티에 @ElementCollection 으로 keywords가 Set<KeywordType>으로 저장되어 있다고 가정
		dto.keywords = reviews.getKeywords().stream()
			.map(KeywordType::name)   // Enum → String
			.toList();

		Appointment appt = reviews.getAppointment();

		// 병원 정보
		dto.hospitalId   = appt.getHospital().getHospitalId();
		dto.hospitalName = appt.getHospital().getName();

		// 의사 정보는 User 엔티티로부터
		Doctor doc       = appt.getDoctor();
		dto.doctorId     = doc.getDoctorId();
		dto.doctorName   = doc.getUser().getName();


		return dto;
	}
}

