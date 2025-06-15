package com.ssginc8.docto.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoShowDetectionService {

	private final AppointmentProvider appointmentProvider;
	private final UserProvider userProvider;

	/**
	 * 어제 날짜 기준으로 완료되지 않은 예약(노쇼 의심)을 조회하여
	 * 직접 취소하지 않고 예약시간을 넘긴 노쇼 예약에 대해 패널티를 부여한다.
	 *
	 * (예약 1시간 이내 취소 시 패널티는 cancelAppointment()에서 이미 부여됨)
	 *
	 * 즉, 병원 관리자 또는 보호자가 노쇼임에도 취소를 안 한 경우를 대비한 보완 로직.
	 */
	@Transactional
	public void detectAndApplyNoShowPenalties() {
		// 어제 00:00 ~ 오늘 00:00까지 범위 설정
		LocalDate today = LocalDate.now();
		LocalDateTime startOfYesterday = today.minusDays(1).atStartOfDay();
		LocalDateTime endOfYesterday = today.atStartOfDay();    // 오늘 자정 전까지

		// 1) 어제 노쇼인 예약 (완료나 취소되지 않은 예약) 을 찾아 패널티 1회씩 부여
		List<Appointment> noShowCandidates = appointmentProvider
			.getAppointmentsNotCompletedBetween(startOfYesterday, endOfYesterday);

		for (Appointment appointment : noShowCandidates) {
			User user = appointment.getPatientGuardian().getUser();
			user.addPenalty(1L);
			userProvider.save(user);
		}

		// 2) 최근 3일 이내 노쇼 예약이 2건 이상인 유저 정지
		LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

		Long threshold = 2L;
		List<User> userToSuspend =  appointmentProvider.getUsersWithNoShowSince(threeDaysAgo, threshold);

		for (User user : userToSuspend) {
			user.suspendFordDays(7);
			userProvider.save(user);
		}

	}
}

