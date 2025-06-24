// src/main/java/com/ssginc8/docto/insurance/exception/DocumentNotFoundException.java
package com.ssginc8.docto.global.error.exception.insuranceException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 전역 예외 처리(ErrorCode 사용) 원칙에 따라
 * 문서를 찾지 못한 경우 던질 커스텀 예외
 */
public class DocumentNotFoundException extends BusinessBaseException {
	public DocumentNotFoundException() {
		super(ErrorCode.INSURANCE_DOCUMENT_NOT_FOUND);
	}
}
