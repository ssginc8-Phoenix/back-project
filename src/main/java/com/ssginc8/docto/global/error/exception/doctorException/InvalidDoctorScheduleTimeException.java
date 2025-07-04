// InvalidDoctorScheduleTimeException.java
package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleTimeException extends BusinessBaseException {

	public InvalidDoctorScheduleTimeException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidDoctorScheduleTimeException() {
		super(ErrorCode.INVALID_DOCTOR_SCHEDULE_TIME);
	}
}
