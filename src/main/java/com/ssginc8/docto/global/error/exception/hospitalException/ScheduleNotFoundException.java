package com.ssginc8.docto.global.error.exception.hospitalException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class ScheduleNotFoundException extends BusinessBaseException {
	public ScheduleNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ScheduleNotFoundException() {
		super(ErrorCode.SCHEDULE_NOT_FOUND);
	}
}
