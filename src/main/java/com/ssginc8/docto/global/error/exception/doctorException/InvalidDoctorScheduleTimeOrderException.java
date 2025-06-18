package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleTimeOrderException extends BusinessBaseException {
	public InvalidDoctorScheduleTimeOrderException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidDoctorScheduleTimeOrderException() {
		super(ErrorCode.INVALID_DOCTOR_SCHEDULE_TIME_ORDER);
	}
}
