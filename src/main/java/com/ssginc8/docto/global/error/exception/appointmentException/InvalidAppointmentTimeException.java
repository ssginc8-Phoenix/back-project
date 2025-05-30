package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidAppointmentTimeException extends BusinessBaseException {
	public InvalidAppointmentTimeException(ErrorCode errorCode) { super (errorCode);}

	public InvalidAppointmentTimeException() { super(ErrorCode.INVALID_APPOINTMENT_TIME);}
}
