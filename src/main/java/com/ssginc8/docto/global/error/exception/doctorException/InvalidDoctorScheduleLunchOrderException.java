package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleLunchOrderException extends BusinessBaseException {
	public InvalidDoctorScheduleLunchOrderException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidDoctorScheduleLunchOrderException() {
		super(ErrorCode.INVALID_DOCTOR_SCHEDULE_LUNCH_ORDER);
	}
}
