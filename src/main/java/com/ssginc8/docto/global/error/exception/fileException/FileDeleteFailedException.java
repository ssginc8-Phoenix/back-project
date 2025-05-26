package com.ssginc8.docto.global.error.exception.fileException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class FileDeleteFailedException extends BusinessBaseException {
	public FileDeleteFailedException(ErrorCode errorCode) {
		super(errorCode);
	}

	public FileDeleteFailedException() {
		super(ErrorCode.FILE_DELETE_FAILED);
	}
}
