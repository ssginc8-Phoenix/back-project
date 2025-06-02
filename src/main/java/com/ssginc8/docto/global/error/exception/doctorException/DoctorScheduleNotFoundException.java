// DoctorScheduleNotFoundException.java
package com.ssginc8.docto.global.error.exception.doctorException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DoctorScheduleNotFoundException extends BusinessBaseException {

  public DoctorScheduleNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

  public DoctorScheduleNotFoundException() {
    super(ErrorCode.DOCTOR_SCHEDULE_NOT_FOUND);
  }
}
