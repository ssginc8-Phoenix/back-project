package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 복약 시간을 찾을 수 없는 경우
 */
public class MedicationAlertTimeNotFoundException extends BusinessBaseException {

	public MedicationAlertTimeNotFoundException() {
		super(ErrorCode.MEDICATION_ALERT_TIME_NOT_FOUND);
	}
}
