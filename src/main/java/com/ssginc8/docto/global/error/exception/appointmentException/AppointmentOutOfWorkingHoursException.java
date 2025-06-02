package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentOutOfWorkingHoursException extends BusinessBaseException {
	public AppointmentOutOfWorkingHoursException(ErrorCode errorCode) { super (errorCode);}

	public AppointmentOutOfWorkingHoursException() { super(ErrorCode.APPOINTMENT_OUT_OF_WORKING_HOURS);}
}
