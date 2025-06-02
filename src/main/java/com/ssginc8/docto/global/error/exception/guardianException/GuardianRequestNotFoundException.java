package com.ssginc8.docto.global.error.exception.guardianException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 보호자 요청을 찾을 수 없을 때 발생
 */
public class GuardianRequestNotFoundException extends BusinessBaseException {
	public GuardianRequestNotFoundException() {
		super(ErrorCode.GUARDIAN_REQUEST_NOT_FOUND);
	}
}