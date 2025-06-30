package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class HospitalScheduleNotFoundException extends BusinessBaseException {

	public HospitalScheduleNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public HospitalScheduleNotFoundException() {
		super(ErrorCode.HOSPITAL_SCHEDULE_NOT_FOUND);
	}
}
