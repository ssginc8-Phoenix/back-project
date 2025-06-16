package com.ssginc8.docto.global.error;

import org.springframework.http.HttpStatus;

import com.google.api.Http;

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
	USER_MISMATCH(HttpStatus.UNAUTHORIZED, "U_009", "요청한 유저 정보가 일치하지 않습니다."),

	// Patient 관련 에러 (P_)
	PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P_001", "환자 정보를 찾을 수 없습니다."),
	ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "G_004", "개인정보 암호화에 실패했습니다."),

	// Guardian 관련 에러 (GDN_)
	GUARDIAN_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "GDN_001", "보호자 요청 정보를 찾을 수 없습니다."),
	GUARDIAN_MAPPING_NOT_FOUND(HttpStatus.NOT_FOUND, "GDN_002", "보호자-환자 매핑 정보를 찾을 수 없습니다."),
	INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "GDN_003", "유효하지 않은 초대 코드입니다."),
	INVALID_GUARDIAN_STATUS(HttpStatus.BAD_REQUEST, "GDN_004", "유효하지 않은 보호자 상태입니다."),
	GUARDIAN_ALREADY_EXISTS(HttpStatus.CONFLICT, "GDN_005", "이미 초대된 보호자입니다."),

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

	//Doctor 관련 에러(D_)
	DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "D_001", "해당 의사가 존재하지 않습니다."),
	SCHEDULE_NOT_IN_DOCTOR(HttpStatus.BAD_REQUEST, "D_002", "해당 스케줄은 해당 의사에게 속하지 않습니다."),
	USER_IS_NOT_DOCTOR(HttpStatus.BAD_REQUEST, "D_003", "해당 유저는 의사 역할이 아닙니다."),
	DOCTOR_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "D_004", "해당 요일의 의사 스케줄이 존재하지 않습니다."),
	INVALID_DOCTOR_SCHEDULE_OUT_OF_HOURS(HttpStatus.BAD_REQUEST, "D_005", "예약 시간이 진료 시간 외입니다."),
	INVALID_DOCTOR_SCHEDULE_LUNCH(HttpStatus.BAD_REQUEST, "D_006", "예약 시간이 점심 시간입니다."),
	HOSPITAL_SCHEDULE_NOT_FOUND(HttpStatus.BAD_REQUEST, "D_007", "병원의 해당 요일 운영 시간이 존재하지 않습니다."),
	INVALID_DOCTOR_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, "D_008", "의사 스케줄이 병원 운영시간 범위를 벗어났습니다."),
	DOCTOR_LUNCH_TIME_CONFLICT(HttpStatus.BAD_REQUEST, "D_009", "의사의 점심시간이 병원 점심시간 범위를 벗어났습니다."),
	DOCTOR_ALREADY_EXISTS(HttpStatus.CONFLICT,"D_010", "이미 의사로 등록된 사용자입니다"),
	NEGATIVE_CAPACITY(HttpStatus.BAD_REQUEST, "D_011", "진료 가능 인원은 음수일 수 없습니다."),
	DOCTOR_OVER_CAPACITY(HttpStatus.BAD_REQUEST, "D_012", "해당 시간에는 이미 예약이 가득 찼습니다."),
	INVALID_DOCTOR_SCHEDULE_REQUIRED_FIELDS(   HttpStatus.BAD_REQUEST, "D_013", "요일, 진료 시작시간, 종료시간은 필수 입력 항목입니다."),
	INVALID_DOCTOR_SCHEDULE_TIME_ORDER(        HttpStatus.BAD_REQUEST, "D_014", "진료 시작시간은 종료시간보다 빠르게 설정해야 합니다."),
	INVALID_DOCTOR_SCHEDULE_LUNCH_INCOMPLETE(   HttpStatus.BAD_REQUEST, "D_015", "점심시간을 설정하려면 시작시간과 종료시간을 모두 입력해야 합니다."),
	INVALID_DOCTOR_SCHEDULE_LUNCH_ORDER(        HttpStatus.BAD_REQUEST, "D_016", "점심 시작시간은 점심 종료시간보다 빠르게 설정해야 합니다."),
	INVALID_DOCTOR_SCHEDULE_LUNCH_RANGE(        HttpStatus.BAD_REQUEST, "D_017", "점심시간은 진료시간 범위 안에 있어야 합니다."),
	INVALID_DOCTOR_SCHEDULE_SLOT_UNIT(          HttpStatus.BAD_REQUEST, "D_020", "스케줄은 30분 단위로만 설정할 수 있습니다."),
	DOCTOR_SCHEDULE_DUPLICATE_DAY(              HttpStatus.BAD_REQUEST, "D_021", "스케줄이 요청 목록 내에 중복되었습니다."),
	DOCTOR_SCHEDULE_OVERLAP(                    HttpStatus.BAD_REQUEST, "D_022", "등록 스케줄이 기존 스케줄과 겹칩니다."),

	//Hospital 관련 에러(H_)
	HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND, "H_001", "해당 병원은 존재하지 않습니다."),
	SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "H_002", "스케줄을 찾을 수 없습니다."),
	SCHEDULE_NOT_IN_HOSPITAL(HttpStatus.BAD_REQUEST, "H_003", "해당 스케줄은 병원에 속하지 않습니다."),
	SCHEDULE_NOT_FOUND_BY_DAY(HttpStatus.NOT_FOUND, "H_004", "해당 요일에 대한 병원 스케줄이 존재하지 않습니다."),
  
	// QNA 관련 에러(Q_)
	QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "Q_001", "Q&A 게시글이 없습니다."),

	// Comment 관련 에러(C_)
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C_001", "답변이 없습니다."),

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
	ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "A_012", "유효하지 않은 역할입니다."),

	// Medication 관련 에러 (MD_)
	MEDICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "MD_001", "약 정보를 찾을 수 없습니다."),
	MEDICATION_ALERT_TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "MD_002", "복약 시간을 찾을 수 없습니다."),
	MEDICATION_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "MD_003", "복약 기록을 찾을 수 없습니다."),
  
  // Review 관련 에러(R_)
	REVIEW_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "R_001", "리뷰가 없습니다."),

	//Notification 관련 에러 (N_)
	NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "N_001", "알림을 찾을 수 없습니다."),
	NOTIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "N_002", "알림 전송에 실패하였습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
