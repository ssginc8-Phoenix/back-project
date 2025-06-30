package com.ssginc8.docto.global.error.exception.hospitalException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidHospitalScheduleTimeOrderException extends BusinessBaseException {
	public InvalidHospitalScheduleTimeOrderException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidHospitalScheduleTimeOrderException() {
		super(ErrorCode.INVALID_HOSPITAL_SCHEDULE_TIME_ORDER);
	}
}
