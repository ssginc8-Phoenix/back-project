package com.ssginc8.docto.global.error.exception.guardianException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

/**
 * 보호자-환자 매핑이 없을 때 발생
 */
public class GuardianMappingNotFoundException extends BusinessBaseException {
	public GuardianMappingNotFoundException() {
		super(ErrorCode.GUARDIAN_MAPPING_NOT_FOUND);
	}
}