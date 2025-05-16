package com.ssginc8.docto.review.entities;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
	@JoinColumn(name = "userId", nullable = false)
	private User author;


	//리뷰내용
	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;


	//신고 수 기본값 0
	@Column(name = "reportCount", nullable = false)
	private Long reportCount = 0L;


    //신고 수 +1
	public void incrementReportCount() {
		this.reportCount++;
	}


	@OneToMany(
		mappedBy = "review",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<ReviewKeyword> keywords = new ArrayList<>();


	public static Review create(Appointment appointment,
		Doctor doctor,
		User author,
		String content) {
		return new Review(appointment, doctor, author, content);
	}


	protected Review(Appointment appointment,
		Doctor doctor,
		User author,
		String content) {
		this.appointment = appointment;
		this.doctor      = doctor;
		this.author      = author;
		this.content     = content;
	}

	// //키워드 연관관계 편의 메서드
	// public void addKeyword(ReviewKeyword kw) {
	// 	keywords.add(kw);
	// 	kw.setReview(this);
	// }




}
