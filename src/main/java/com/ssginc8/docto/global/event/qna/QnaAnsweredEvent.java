package com.ssginc8.docto.global.event.qna;

import com.ssginc8.docto.qna.entity.QaComment;

import lombok.Getter;

@Getter
public class QnaAnsweredEvent {

	private final QaComment qaComment;

	public QnaAnsweredEvent(QaComment qaComment) {
		this.qaComment = qaComment;
	}
}
