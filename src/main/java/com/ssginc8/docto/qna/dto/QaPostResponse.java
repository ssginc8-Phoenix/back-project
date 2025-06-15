package com.ssginc8.docto.qna.dto;

import java.time.LocalDateTime;

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

	public static QaPostResponse fromEntity(QaPost post) {
		QaPostResponse dto = new QaPostResponse();
		dto.qnaPostId     = post.getQnaPostId();
		dto.appointmentId = post.getAppointment().getAppointmentId();
		dto.content       = post.getContent();
		dto.status        = post.getStatus();
		dto.createdAt     = post.getCreatedAt();
		dto.updatedAt     = post.getUpdatedAt();
		return dto;
	}
}
