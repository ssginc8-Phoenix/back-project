package com.ssginc8.docto.global.error.exception.reviewException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class ReviewNotFoundException extends BusinessBaseException {
	public ReviewNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ReviewNotFoundException() {
		super(ErrorCode.REVIEW_NOT_FOUND);
	}
}
