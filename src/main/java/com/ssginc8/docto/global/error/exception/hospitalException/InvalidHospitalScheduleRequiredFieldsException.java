package com.ssginc8.docto.global.error.exception.hospitalException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidHospitalScheduleRequiredFieldsException extends BusinessBaseException {
	public InvalidHospitalScheduleRequiredFieldsException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidHospitalScheduleRequiredFieldsException() {
		super(ErrorCode.INVALID_HOSPITAL_SCHEDULE_REQUIRED_FIELDS);
	}
}
