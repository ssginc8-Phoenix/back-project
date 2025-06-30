package com.ssginc8.docto.qna.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.entity.QaStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class QaPostResponse {

	private Long qnaPostId;
	private Long appointmentId;

	@NotBlank
	private String content;
	private QaStatus status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private String guardianName;
	private String patientName;
	private String symptom;
	private String appointmentTime;

	public static QaPostResponse fromEntity(QaPost post) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E) HH:mm", Locale.KOREAN);

		QaPostResponse dto = new QaPostResponse();
		dto.qnaPostId     = post.getQnaPostId();
		dto.appointmentId = post.getAppointment().getAppointmentId();
		dto.content       = post.getContent();
		dto.status        = post.getStatus();
		dto.createdAt     = post.getCreatedAt();
		dto.updatedAt     = post.getUpdatedAt();
		dto.guardianName     = post.getAppointment().getPatientGuardian().getUser().getName();
		dto.patientName      = post.getAppointment().getPatientGuardian().getPatient().getUser().getName();
		dto.symptom          = post.getAppointment().getSymptom();
		dto.appointmentTime  = post.getAppointment().getAppointmentTime().format(formatter);

		return dto;
	}
}
