package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class AppointmentCompletedModificationNotAllowedException extends BusinessBaseException {
	public AppointmentCompletedModificationNotAllowedException(ErrorCode errorCode) { super (errorCode);}

	public AppointmentCompletedModificationNotAllowedException() {
		super(ErrorCode.APPOINTMENT_COMPLETED_MODIFICATION_NOT_ALLOWED);
	}
}
