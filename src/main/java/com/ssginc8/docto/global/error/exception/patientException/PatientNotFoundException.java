package com.ssginc8.docto.global.error.exception.patientException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 환자 정보를 찾을 수 없을 때 발생하는 예외
 */
public class PatientNotFoundException extends BusinessBaseException {
	public PatientNotFoundException() {
		super(ErrorCode.PATIENT_NOT_FOUND);
	}
}
