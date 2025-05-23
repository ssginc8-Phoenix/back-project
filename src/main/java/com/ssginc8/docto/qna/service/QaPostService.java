package com.ssginc8.docto.qna.service;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.qna.entity.QaPost;

public interface QaPostService {

	QaPost createQaPost(Appointment appointment, String qaPost);
}
