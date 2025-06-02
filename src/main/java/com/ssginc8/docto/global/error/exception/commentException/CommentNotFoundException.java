package com.ssginc8.docto.global.error.exception.commentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class CommentNotFoundException extends BusinessBaseException {
	public CommentNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public CommentNotFoundException() {
		super(ErrorCode.COMMENT_NOT_FOUND);
	}
}
