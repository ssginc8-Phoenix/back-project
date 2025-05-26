package com.ssginc8.docto.qna.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.qna.entity.QaComment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {

	private Long commentId;
	private Long qnaPostId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static CommentResponse fromEntity(QaComment comment) {
		CommentResponse dto = new CommentResponse();
		dto.commentId  = comment.getQnaCommentId();
		dto.qnaPostId  = comment.getQnaPostId().getQnaPostId();
		dto.content    = comment.getContent();
		dto.createdAt  = comment.getCreatedAt();
		dto.updatedAt  = comment.getUpdatedAt();
		return dto;
	}


}
