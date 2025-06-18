package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleRequiredFieldsException extends BusinessBaseException {
  public InvalidDoctorScheduleRequiredFieldsException(ErrorCode errorCode) {
    super(errorCode);
  }

  public InvalidDoctorScheduleRequiredFieldsException() {
    super(ErrorCode.INVALID_DOCTOR_SCHEDULE_REQUIRED_FIELDS);
  }
}
