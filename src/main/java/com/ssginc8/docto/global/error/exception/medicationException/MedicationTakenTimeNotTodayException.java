package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class MedicationTakenTimeNotTodayException extends BusinessBaseException {
	public MedicationTakenTimeNotTodayException(ErrorCode errorCode) { super (errorCode);}

	public MedicationTakenTimeNotTodayException() {
		super(ErrorCode.MEDICATION_TAKEN_TIME_NOT_TODAY);
	}
}
