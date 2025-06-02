package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class NotDoctorRoleException extends BusinessBaseException {

	public NotDoctorRoleException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NotDoctorRoleException() {
		super(ErrorCode.USER_IS_NOT_DOCTOR);
	}
}
