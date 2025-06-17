package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleLunchRangeException extends BusinessBaseException {
	public InvalidDoctorScheduleLunchRangeException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidDoctorScheduleLunchRangeException() {
		super(ErrorCode.INVALID_DOCTOR_SCHEDULE_LUNCH_RANGE);
	}
}
