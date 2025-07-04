package com.ssginc8.docto.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessageRequest {

	/** 유저 아이디 */
	private Long userId;

	/** 메세지 제목 */
	private String title;

	/** 메세지 내용 */
	private String body;
}
