package com.ssginc8.docto.global.error.exception.fileException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class FileUploadFailedException extends BusinessBaseException {
	public FileUploadFailedException(ErrorCode errorCode) {
		super(errorCode);
	}

	public FileUploadFailedException() {
		super(ErrorCode.FILE_UPLOAD_FAILED);
	}
}