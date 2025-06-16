package com.ssginc8.docto.global.error.exception.notificationException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class NotificationSendFailed extends BusinessBaseException {
	public NotificationSendFailed(ErrorCode errorCode) { super (errorCode);}

	public NotificationSendFailed() {
		super(ErrorCode.NOTIFICATION_SEND_FAILED);
	}
}
