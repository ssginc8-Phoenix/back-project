package com.ssginc8.docto.global.error.exception.guardianException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 보호자-환자 상태가 유효하지 않을 때 발생
 */
public class InvalidGuardianStatusException extends BusinessBaseException {
	public InvalidGuardianStatusException() {
		super(ErrorCode.INVALID_GUARDIAN_STATUS);
	}
}