package com.ssginc8.docto.global.error.exception.hospitalException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class ScheduleNotFoundByDayException extends BusinessBaseException {
	public ScheduleNotFoundByDayException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ScheduleNotFoundByDayException() {
		super(ErrorCode.SCHEDULE_NOT_FOUND_BY_DAY);
	}
}
