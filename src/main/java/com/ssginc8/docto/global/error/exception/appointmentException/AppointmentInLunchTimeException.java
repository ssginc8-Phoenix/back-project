package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentInLunchTimeException extends BusinessBaseException {
	public AppointmentInLunchTimeException(ErrorCode errorCode) { super (errorCode);}

	public AppointmentInLunchTimeException() { super(ErrorCode.APPOINTMENT_IN_LUNCH_TIME);}
}
