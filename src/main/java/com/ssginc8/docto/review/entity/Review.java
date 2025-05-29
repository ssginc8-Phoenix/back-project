package com.ssginc8.docto.review.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseTimeEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "hospital_id")
	private Hospital hospital;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "doctor_id", nullable = false)
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "appointment_id")
	private Appointment appointment;

	@Column(nullable = false, length = 1000)
	private String contents;


	@Column(nullable = false)
	private Long reportCount;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "tbl_review_keyword",
		joinColumns = @JoinColumn(name = "review_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "keyword", nullable = false)
	private Set<KeywordType> keywords = new HashSet<>();


	//리뷰 생성 팩토리 메서드 (user, contents, keywords 모두 필수)
	public static Review create(
		User user,
		Hospital hospital,
		Doctor doctor,
		Appointment appointment,
		String contents,
		Collection<KeywordType> keywordTypes
	) {
		Review review = new Review();
		review.user = user;
		review.hospital = hospital;
		review.doctor = doctor;
		review.appointment = appointment;
		review.contents = contents;
		review.reportCount = 0L;
		review.keywords.addAll(keywordTypes);
		return review;
	}


	//내용 수정
	public void updateContents(String newContents) {
		this.contents = newContents;
	}


	// 신고 횟수 증가
	public void incrementReportCount() {
		this.reportCount++;
	}
}
