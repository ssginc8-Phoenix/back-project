package com.ssginc8.docto.medication.service;

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
		User user = medicationProvider.getUser(request.getUserId());

		// ✅ 약 정보는 1개만 생성
		MedicationInformation info = MedicationInformation.create(
			user,
			request.getPatientGuardianId(),
			request.getMedicationName(),
			request.getStartDate(),
			request.getEndDate()
		);

		// ✅ 알림 시간도 1개만 생성
		MedicationAlertTime alertTime = MedicationAlertTime.create(info, request.getTimeToTake());

		// ✅ 선택된 요일마다 Day 생성
		for (DayOfWeek day : request.getDays()) {
			alertTime.getAlertDays().add(MedicationAlertDay.create(alertTime, day));
		}

		info.getAlertTimes().add(alertTime);

		// ✅ 단 1회 저장
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
		MedicationInformation info = medicationProvider.getMedication(medicationId);
		MedicationAlertTime alertTime = info.getAlertTimes().get(0); // 첫 번째 알림시간 수정
		alertTime.updateTimeToTake(request.getNewTimeToTake());

		alertTime.getAlertDays().clear();
		List<MedicationAlertDay> newDays = request.getNewDays().stream()
			.map(day -> MedicationAlertDay.create(alertTime, day))
			.collect(Collectors.toList());
		alertTime.getAlertDays().addAll(newDays);
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
