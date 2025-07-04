package com.ssginc8.docto.global.error.exception.guardianException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 이미 초대된 보호자 예외
 */
public class GuardianAlreadyExistsException extends BusinessBaseException {
	public GuardianAlreadyExistsException() {
		super(ErrorCode.GUARDIAN_ALREADY_EXISTS);
	}
}
