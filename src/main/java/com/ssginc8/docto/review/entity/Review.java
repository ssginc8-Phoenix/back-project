package com.ssginc8.docto.review.entity;

import java.util.List;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

	@ManyToOne(fetch = FetchType.LAZY)
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

	public void incrementReportCount() {
		this.reportCount++;
	}

	// ----------- 생성자 -----------
	protected Review(Appointment appointment,
		Doctor doctor,
		Hospital hospital,
		User author,
		String content) {
		this.appointment = appointment;
		this.doctor      = doctor;
		this.hospital    = hospital;
		this.author      = author;
		this.contents     = content;
	}

	// ----------- 팩토리 메서드 -----------
	public static Review create(Appointment appointment,
		Doctor doctor,
		Hospital hospital,
		User author,
		String contents,
		@NotNull(message = "키워드를 최소 3개 선택해주세요.")
		@Size(min = 3, max = 8, message = "키워드는 3~8개 사이로 선택 가능합니다.") List<KeywordType> keywords) {
		return new Review(appointment, doctor, hospital, author, contents);
	}

	public @NotBlank(message = "contents는 빈 값일 수 없습니다.")
	@Size(max = 1000, message = "contents는 최대 1000자까지 입력 가능합니다.")
	String updateContent;
}
