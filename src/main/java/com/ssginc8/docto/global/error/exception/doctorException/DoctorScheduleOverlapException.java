package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorScheduleOverlapException extends BusinessBaseException {
	public DoctorScheduleOverlapException(ErrorCode errorCode) {
		super(errorCode);
	}

	public DoctorScheduleOverlapException() {
		super(ErrorCode.DOCTOR_SCHEDULE_OVERLAP);
	}
}
