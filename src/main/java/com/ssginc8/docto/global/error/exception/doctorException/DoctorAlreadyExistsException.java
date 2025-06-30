package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorAlreadyExistsException extends BusinessBaseException {
  public DoctorAlreadyExistsException(ErrorCode errorCode) {
    super(errorCode);
  }

  public DoctorAlreadyExistsException() {
    super(ErrorCode.DOCTOR_ALREADY_EXISTS);
  }
}
