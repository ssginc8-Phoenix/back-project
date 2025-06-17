package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidMedicationDateException extends BusinessBaseException {
	public InvalidMedicationDateException() {
		super(ErrorCode.INVALID_MEDICATION_DATE);
	}
}
