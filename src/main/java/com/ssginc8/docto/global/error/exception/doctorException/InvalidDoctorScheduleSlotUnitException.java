package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidDoctorScheduleSlotUnitException extends BusinessBaseException {
  public InvalidDoctorScheduleSlotUnitException(ErrorCode errorCode) {
    super(errorCode);
  }

  public InvalidDoctorScheduleSlotUnitException() {
    super(ErrorCode.INVALID_DOCTOR_SCHEDULE_SLOT_UNIT);
  }
}
