package com.ssginc8.docto.global.event.qna;

import java.time.LocalDateTime;

import com.ssginc8.docto.qna.entity.QaComment;
import com.ssginc8.docto.user.entity.User;

import lombok.Getter;

@Getter
public class QnaAnsweredEvent {

	private final Long qnaPostId; // Q&A 게시글 ID
	private final LocalDateTime answeredAt; // Q&A 게시글이 작성된 시간
	private final Long qnaCommentId; // 답변 댓글 ID

	public QnaAnsweredEvent(Long qnaPostId, LocalDateTime answeredAt, Long qnaCommentId) {
		this.qnaPostId = qnaPostId;
		this.answeredAt = answeredAt;
		this.qnaCommentId = qnaCommentId;
	}
}
