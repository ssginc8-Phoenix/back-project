package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentCanceledModificationNotAllowedException extends BusinessBaseException {
	public AppointmentCanceledModificationNotAllowedException(ErrorCode errorCode) { super (errorCode);}

	public AppointmentCanceledModificationNotAllowedException() {
		super(ErrorCode.APPOINTMENT_CANCELED_MODIFICATION_NOT_ALLOWED);
	}
}
