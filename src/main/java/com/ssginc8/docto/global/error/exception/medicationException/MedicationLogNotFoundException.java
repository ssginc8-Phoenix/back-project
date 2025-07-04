package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 복약 기록을 찾을 수 없는 경우
 */
public class MedicationLogNotFoundException extends BusinessBaseException {

	public MedicationLogNotFoundException() {
		super(ErrorCode.MEDICATION_LOG_NOT_FOUND);
	}
}
