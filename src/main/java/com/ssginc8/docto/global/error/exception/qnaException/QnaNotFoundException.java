package com.ssginc8.docto.global.error.exception.qnaException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class QnaNotFoundException extends BusinessBaseException {
	public QnaNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public QnaNotFoundException() {
		super(ErrorCode.QNA_NOT_FOUND);
	}
}
