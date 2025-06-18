package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class MedicationDateRangeInvalidException extends BusinessBaseException {
	public MedicationDateRangeInvalidException() {
		super(ErrorCode.MEDICATION_DATE_RANGE_INVALID);
	}
}
