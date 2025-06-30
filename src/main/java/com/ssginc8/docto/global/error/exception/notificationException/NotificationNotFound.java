package com.ssginc8.docto.global.error.exception.notificationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class NotificationNotFound extends BusinessBaseException {
	public NotificationNotFound(ErrorCode errorCode) { super (errorCode);}

	public NotificationNotFound() {
		super(ErrorCode.NOTIFICATION_NOT_FOUND);
	}
}
