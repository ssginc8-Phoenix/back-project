package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class RoleNotFoundException extends BusinessBaseException {
	public RoleNotFoundException(ErrorCode errorCode) { super (errorCode);}

	public RoleNotFoundException() {
		super(ErrorCode.ROLE_NOT_FOUND);
	}
}
