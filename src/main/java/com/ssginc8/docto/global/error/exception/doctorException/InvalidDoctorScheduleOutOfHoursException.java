package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleOutOfHoursException extends BusinessBaseException {

  public InvalidDoctorScheduleOutOfHoursException(ErrorCode errorCode) {
    super(errorCode);
  }

  public InvalidDoctorScheduleOutOfHoursException() {
    super(ErrorCode.INVALID_DOCTOR_SCHEDULE_OUT_OF_HOURS);
  }
}
