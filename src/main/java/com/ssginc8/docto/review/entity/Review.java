package com.ssginc8.docto.review.entity;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviewId")
	private Long reviewId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointmentId", nullable = false)
	private Appointment appointment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctorId", nullable = false)
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospitalId", nullable = false)
	private Hospital hospital;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User author;

	@Column(name = "contents", columnDefinition = "TEXT", nullable = false)
	private String contents;

	@Column(name = "reportCount", nullable = false)
	private Long reportCount = 0L;

	//유효성 검증
	private static void validateContents(String contents) {
		if (contents == null || contents.isBlank()) {
			throw new IllegalArgumentException("contents는 빈 값일 수 없습니다.");
		}
	}


	public static Review create(
		Appointment appointment,
		Doctor doctor,
		Hospital hospital,
		User author,
		String contents
	) {
		validateContents(contents);
		Review review = new Review();
		review.appointment = appointment;
		review.doctor = doctor;
		review.hospital = hospital;
		review.author = author;
		review.contents = contents;
		review.reportCount = 0L;
		return review;
	}


	//신고수 증가
	public void incrementReportCount() {
		this.reportCount++;
	}


}
