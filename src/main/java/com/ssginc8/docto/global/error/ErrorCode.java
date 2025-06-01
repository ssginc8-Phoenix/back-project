package com.ssginc8.docto.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Global 에러 (G_) - 공통적으로 발생할 수 있는 예외
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G_001", "잘못된 HTTP 메서드를 호출했습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G_002", "서버 에러가 발생했습니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "G_003", "입력값이 유효하지 않습니다."),

	// User 관련 에러(U_)
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U_001", "유저를 찾을 수 없습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U_002", "이미 사용 중인 이메일입니다."),
	PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "U_003", "비밀번호는 최소 8자 이상이어야 합니다."),
	PASSWORD_TOO_SIMPLE(HttpStatus.BAD_REQUEST, "U_004", "비밀번호는 대문자, 소문자, 숫자, 특수문자 중 2가지 이상을 조합해야 합니다."),
	PASSWORD_HAS_SEQUENCE(HttpStatus.BAD_REQUEST, "U_005", "비밀번호에 연속된 문자를 사용할 수 없습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U_006", "비밀번호가 일치하지 않습니다."),
	EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "U_007", "이메일을 찾을 수 없습니다."),
	SAME_AS_PREVIOUS_PASSWORD(HttpStatus.BAD_REQUEST, "U_008", "이전 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),

	// Mail 전송 관련 에러 (M_)
	EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M_001", "메일 전송에 실패했습니다."),
	EMAIL_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "M_002", "이메일 인증에 실패하였습니다."),

	// Token 관련 에러 (T_)
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "T_001", "리프레시 토큰이 유효하지 않습니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "T_002", "엑세스 토큰이 유효하지 않습니다."),
	UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED, "T_003", "인증이 필요합니다."),
	FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, "T_004", "해당 리소스에 접근할 권한이 없습니다."),

	// File 관련 에러(F_)
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F_001", "파일 업로드에 실패했습니다."),
	FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F_002", "파일 삭제에 실패했습니다."),

	// Appointment 관련 에러 (A_)
	APPOINTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "A_001", "예약을 찾을 수 없습니다."),
	INVALID_APPOINTMENT_TIME(HttpStatus.BAD_REQUEST, "A_002", "과거 시간으로 예약할 수 없습니다."),
	DUPLICATE_APPOINTMENT(HttpStatus.CONFLICT, "A_003", "이미 해당 시간에 예약이 존재합니다."),
	APPOINTMENT_OUT_OF_WORKING_HOURS(HttpStatus.BAD_REQUEST, "A_004", "예약 시간이 진료 시간 외입니다."),
	APPOINTMENT_IN_LUNCH_TIME(HttpStatus.BAD_REQUEST, "A_005", "예약 시간이 점심 시간입니다."),
	APPOINTMENT_DUPLICATE_TIME_WINDOW(HttpStatus.CONFLICT, "A_006", "동일 환자의 30분 내 중복 예약은 허용되지 않습니다."),
	APPOINTMENT_COMPLETED_MODIFICATION_NOT_ALLOWED(HttpStatus.CONFLICT, "A_007", "진료가 완료된 예약은 상태를 변경할 수 없습니다."),
	APPOINTMENT_CANCELED_MODIFICATION_NOT_ALLOWED(HttpStatus.CONFLICT, "A_008", "취소된 예약은 상태를 변경할 수 없습니다."),
	INVALID_STATUS_VALUE(HttpStatus.BAD_REQUEST, "A_009", "유효하지 않은 예약 상태입니다."),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "A_010", "유효하지 않은 예약 타입입니다."),
	INVALID_PAYMENT_VALUE(HttpStatus.BAD_REQUEST, "A_011", "유효하지 않은 결제 방법입니다."),

	// Doctor 관련 에러 (D_)
	NEGATIVE_CAPACITY(HttpStatus.BAD_REQUEST, "D_001", "진료 가능 인원은 음수일 수 없습니다."),
	DOCTOR_OVER_CAPACITY(HttpStatus.BAD_REQUEST, "D_002", "해당 시간에는 이미 예약이 가득 찼습니다."),

	// Doctor Schedule 관련 에러 (DS_)
	DOCTOR_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "DS_001", "의사의 스케쥴이 존재하지 않습니다."),

	// Medication 관련 에러 (MD_)
	MEDICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "MD_001", "약 정보를 찾을 수 없습니다."),
	MEDICATION_ALERT_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "MD_002", "복약 시간을 찾을 수 없습니다."),
	MEDICATION_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "MD_003", "복약 기록을 찾을 수 없습니다.");


	private final HttpStatus status;
	private final String code;
	private final String message;
}
