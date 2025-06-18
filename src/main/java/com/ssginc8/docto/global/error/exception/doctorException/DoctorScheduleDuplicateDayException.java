package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorScheduleDuplicateDayException extends BusinessBaseException {
  public DoctorScheduleDuplicateDayException(ErrorCode errorCode) {
    super(errorCode);
  }

  public DoctorScheduleDuplicateDayException() {
    super(ErrorCode.DOCTOR_SCHEDULE_DUPLICATE_DAY);
  }
}
