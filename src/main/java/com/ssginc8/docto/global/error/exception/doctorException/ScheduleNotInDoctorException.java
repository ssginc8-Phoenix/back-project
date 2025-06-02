package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class ScheduleNotInDoctorException extends BusinessBaseException {

	public ScheduleNotInDoctorException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ScheduleNotInDoctorException() {
		super(ErrorCode.SCHEDULE_NOT_IN_DOCTOR);
	}
}
