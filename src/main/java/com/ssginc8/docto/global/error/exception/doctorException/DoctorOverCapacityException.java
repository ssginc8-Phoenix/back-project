package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorOverCapacityException extends BusinessBaseException {
	public DoctorOverCapacityException(ErrorCode errorCode) { super (errorCode);}

	public DoctorOverCapacityException() { super(ErrorCode.DOCTOR_OVER_CAPACITY);}
}
