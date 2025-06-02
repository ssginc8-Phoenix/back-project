package com.ssginc8.docto.global.error.exception.hospitalException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class ScheduleNotInHospitalException extends BusinessBaseException {
	public ScheduleNotInHospitalException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ScheduleNotInHospitalException() {
		super(ErrorCode.SCHEDULE_NOT_IN_HOSPITAL);
	}
}
