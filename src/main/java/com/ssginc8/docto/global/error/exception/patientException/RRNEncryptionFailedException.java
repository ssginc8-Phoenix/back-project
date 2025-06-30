package com.ssginc8.docto.global.error.exception.patientException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 주민등록번호 암호화 실패 시 발생하는 예외
 */
public class RRNEncryptionFailedException extends BusinessBaseException {
	public RRNEncryptionFailedException() {
		super(ErrorCode.ENCRYPTION_FAILED);
	}
}
