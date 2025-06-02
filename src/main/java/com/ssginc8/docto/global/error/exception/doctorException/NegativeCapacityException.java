package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class NegativeCapacityException extends BusinessBaseException {
	public NegativeCapacityException(ErrorCode errorCode) { super (errorCode);}

	public NegativeCapacityException() { super(ErrorCode.NEGATIVE_CAPACITY);}
}
