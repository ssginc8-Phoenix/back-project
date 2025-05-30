package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DuplicateAppointmentException extends BusinessBaseException {
	public DuplicateAppointmentException(ErrorCode errorCode) { super (errorCode);}

	public DuplicateAppointmentException() { super(ErrorCode.DUPLICATE_APPOINTMENT);}
}
