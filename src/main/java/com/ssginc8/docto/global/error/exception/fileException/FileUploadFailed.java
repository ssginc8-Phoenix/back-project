package com.ssginc8.docto.global.error.exception.fileException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class FileUploadFailed extends BusinessBaseException {
	public FileUploadFailed(ErrorCode errorCode) {
		super(errorCode);
	}

	public FileUploadFailed() {
		super(ErrorCode.FILE_UPLOAD_FAILED);
	}
}