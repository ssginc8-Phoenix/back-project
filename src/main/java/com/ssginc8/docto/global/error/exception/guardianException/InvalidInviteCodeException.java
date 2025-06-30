package com.ssginc8.docto.global.error.exception.guardianException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 초대 코드가 유효하지 않을 때 발생
 */
public class InvalidInviteCodeException extends BusinessBaseException {
	public InvalidInviteCodeException() {
		super(ErrorCode.INVALID_INVITE_CODE);
	}
}