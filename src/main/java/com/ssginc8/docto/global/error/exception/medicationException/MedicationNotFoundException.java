package com.ssginc8.docto.global.error.exception.medicationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 약 정보를 찾을 수 없는 경우
 */
public class MedicationNotFoundException extends BusinessBaseException {

	public MedicationNotFoundException() {
		super(ErrorCode.MEDICATION_NOT_FOUND);
	}
}
