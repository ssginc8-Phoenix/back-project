package com.ssginc8.docto.global.error.exception.hospitalException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class HospitalNotFoundException extends BusinessBaseException {
	public HospitalNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

    public HospitalNotFoundException() {
      super(ErrorCode.HOSPITAL_NOT_FOUND);
    }
}
