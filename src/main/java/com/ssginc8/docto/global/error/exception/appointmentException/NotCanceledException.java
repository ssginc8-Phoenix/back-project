package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class NotCanceledException extends BusinessBaseException {
	public NotCanceledException(ErrorCode errorCode) { super (errorCode);}

	public NotCanceledException() {super(ErrorCode.NOT_CANCELED);}
}
