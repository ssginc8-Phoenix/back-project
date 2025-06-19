package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class MedicationAlertDayNotFoundException extends BusinessBaseException {
	public MedicationAlertDayNotFoundException() {
		super(ErrorCode.MEDICATION_ALERT_DAY_NOT_FOUNT);
	}
}
