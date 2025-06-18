package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleLunchIncompleteException extends BusinessBaseException {
	public InvalidDoctorScheduleLunchIncompleteException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidDoctorScheduleLunchIncompleteException() {
		super(ErrorCode.INVALID_DOCTOR_SCHEDULE_LUNCH_INCOMPLETE);
	}
}
