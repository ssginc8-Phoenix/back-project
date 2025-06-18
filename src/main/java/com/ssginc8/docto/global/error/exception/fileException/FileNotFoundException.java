package com.ssginc8.docto.global.error.exception.fileException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class FileNotFoundException extends BusinessBaseException {
	public FileNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public FileNotFoundException() {
		super(ErrorCode.FILE_NOT_FOUNT);
	}

}
