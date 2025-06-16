package com.ssginc8.docto.medication.service;

import com.ssginc8.docto.global.error.exception.medicationException.InvalidMedicationDateException;
import com.ssginc8.docto.global.error.exception.medicationException.MedicationAlertDayNotFoundException;
import com.ssginc8.docto.global.error.exception.medicationException.MedicationAlertTimeNotFoundException;
import com.ssginc8.docto.global.error.exception.medicationException.MedicationDateRangeInvalidException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.medication.dto.*;
import com.ssginc8.docto.medication.entity.*;
import com.ssginc8.docto.medication.provider.MedicationProvider;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

	private final MedicationProvider medicationProvider;
	private final PatientGuardianProvider patientGuardianProvider;

	@Transactional(readOnly = true)
	@Override
	public Page<MedicationLogResponse> getMedicationLogsByCurrentUser(Pageable pageable) {
		Page<MedicationLog> logs = medicationProvider.getMedicationLogsByCurrentUser(pageable);
		return logs.map(MedicationLogResponse::from);
	}

	@Transactional
	@Override
	public void registerMedicationSchedule(MedicationScheduleRequest request) {
		// 날짜 유효성 검사
		if (request.getStartDate() != null && request.getEndDate() != null &&
			request.getStartDate().isAfter(request.getEndDate())) {
			// 예외 클래스는 ErrorCode에 맞춰 미리 구현해두었다고 가정
			throw new InvalidMedicationDateException(); // ErrorCode.INVALID_MEDICATION_DATE
		}

		User user = medicationProvider.getUser(request.getUserId());
		// (추가로 patientGuardian 검사 등 필요시 여기에)

		MedicationInformation info = MedicationInformation.create(
			user,
			request.getPatientGuardianId(),
			request.getMedicationName(),
			request.getStartDate(),
			request.getEndDate()
		);

		MedicationAlertTime alertTime = MedicationAlertTime.create(info, request.getTimeToTake());
		List<DayOfWeek> days = request.getDays();
		if (days == null || days.isEmpty()) {
			throw new MedicationAlertDayNotFoundException();
			// 또는 BAD_REQUEST 예외: ErrorCode.MEDICATION_ALERT_TIME_NOT_FOUND 같은 적절한 코드
		}
		for (DayOfWeek day : days) {
			alertTime.getAlertDays().add(MedicationAlertDay.create(alertTime, day));
		}
		info.getAlertTimes().add(alertTime);

		medicationProvider.saveMedicationInformation(info);
	}


	@Transactional(readOnly = true)
	@Override
	public List<MedicationScheduleResponse> getMedicationSchedulesByCurrentUser() {
		User user = medicationProvider.getCurrentUserFromToken();

		List<MedicationInformation> infos = medicationProvider.getMedicationsByUser(user);

		return infos.stream()
			.flatMap(info -> info.getAlertTimes().stream().map(alertTime -> {
				List<DayOfWeek> days = alertTime.getAlertDays().stream()
					.map(MedicationAlertDay::getDayOfWeek)
					.collect(Collectors.toList());
				return MedicationScheduleResponse.from(info, alertTime, days);
			}))
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void updateMedicationTime(Long medicationId, MedicationUpdateRequest request) {
		// 1) 날짜 범위 변경 처리
		LocalDate newStart = request.getNewStartDate();
		LocalDate newEnd = request.getNewEndDate();
		if (newStart != null || newEnd != null) {
			if (newStart == null || newEnd == null) {
				throw new MedicationDateRangeInvalidException();
			}
			if (newStart.isAfter(newEnd)) {
				throw new InvalidMedicationDateException();
			}
			// Provider로 위임
			medicationProvider.updateDateRange(medicationId, newStart, newEnd);
		}
		// 2) 시간/요일 변경: bulk update 후 다시 로드
		MedicationInformation info = medicationProvider.getMedication(medicationId);
		List<MedicationAlertTime> alertTimes = info.getAlertTimes();
		if (alertTimes == null || alertTimes.isEmpty()) {
			throw new MedicationAlertTimeNotFoundException();
		}
		MedicationAlertTime alertTime = alertTimes.get(0);

		if (request.getNewTimeToTake() != null) {
			alertTime.updateTimeToTake(request.getNewTimeToTake());
		}
		List<DayOfWeek> newDays = request.getNewDays();
		if (newDays != null) {
			if (newDays.isEmpty()) {
				throw new MedicationAlertDayNotFoundException();
			}
			alertTime.getAlertDays().clear();
			for (DayOfWeek day : newDays) {
				alertTime.getAlertDays().add(MedicationAlertDay.create(alertTime, day));
			}
		}
	}

	@Transactional
	@Override
	public void deleteMedicationSchedule(Long medicationId) {
		MedicationInformation info = medicationProvider.getMedication(medicationId);
		info.delete(); // soft delete
	}

	@Transactional
	@Override
	public void completeMedication(Long medicationId, MedicationCompleteRequest request) {
		MedicationInformation info = medicationProvider.getMedication(medicationId);

		// 복약 스케줄 (알림시간) 가져오기
		List<MedicationAlertTime> alertTimes = info.getAlertTimes();

		for (MedicationAlertTime alertTime : alertTimes) {
			MedicationLog log = MedicationLog.create(
				alertTime,
				info,
				request.getStatus(),
				alertTime.getTimeToTake().atDate(LocalDate.now())
			);
			medicationProvider.saveMedicationLog(log);
		}
	}


}
