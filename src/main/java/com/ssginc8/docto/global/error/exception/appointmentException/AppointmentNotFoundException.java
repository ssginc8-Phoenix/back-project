package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentNotFoundException extends BusinessBaseException {
	public AppointmentNotFoundException(ErrorCode errorCode) { super (errorCode);}

	public AppointmentNotFoundException() { super(ErrorCode.APPOINTMENT_NOT_FOUND);}
}
