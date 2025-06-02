package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentDuplicateTimeWindowException extends BusinessBaseException {
	public AppointmentDuplicateTimeWindowException(ErrorCode errorCode) { super (errorCode);}

	public AppointmentDuplicateTimeWindowException() { super(ErrorCode.APPOINTMENT_DUPLICATE_TIME_WINDOW);}
}
