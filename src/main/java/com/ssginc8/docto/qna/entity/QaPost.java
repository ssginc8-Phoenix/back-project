package com.ssginc8.docto.qna.entity;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_qa_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class QaPost extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qnaPostId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointmentId", nullable = false)
	private Appointment appointment;

	@Setter
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	public static QaPost create(Appointment appointment, String content) {
		QaPost qaPost = new QaPost();
		qaPost.appointment = appointment;
		qaPost.content = content;
		return qaPost;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private QaStatus status = QaStatus.PENDING;

	// 상태 변경 메서드 추가
	public void changeStatus(QaStatus newStatus) {
		this.status = newStatus;
	}


}
