package com.ssginc8.docto.global.error.exception.guardianException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class GuardianNotFoundException extends BusinessBaseException {
	public GuardianNotFoundException(ErrorCode errorCode) { super (errorCode);}

	public GuardianNotFoundException() {
		super(ErrorCode.GUARDIAN_NOT_FOUND);
	}

}
