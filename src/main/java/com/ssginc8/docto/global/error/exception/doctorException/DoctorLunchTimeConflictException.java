// DoctorLunchTimeConflictException.java
package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorLunchTimeConflictException extends BusinessBaseException {

	public DoctorLunchTimeConflictException(ErrorCode errorCode) {
		super(errorCode);
	}

	public DoctorLunchTimeConflictException() {
		super(ErrorCode.DOCTOR_LUNCH_TIME_CONFLICT);
	}
}
