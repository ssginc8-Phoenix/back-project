package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorNotFoundException extends BusinessBaseException {

	public DoctorNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public DoctorNotFoundException() {
		super(ErrorCode.DOCTOR_NOT_FOUND);
	}
}
