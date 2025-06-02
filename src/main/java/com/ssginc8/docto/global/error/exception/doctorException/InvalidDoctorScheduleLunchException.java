package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleLunchException extends BusinessBaseException {

	public InvalidDoctorScheduleLunchException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidDoctorScheduleLunchException() {
		super(ErrorCode.INVALID_DOCTOR_SCHEDULE_LUNCH);
	}
}
