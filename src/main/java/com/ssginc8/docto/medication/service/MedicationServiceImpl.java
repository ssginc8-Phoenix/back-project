package com.ssginc8.docto.medication.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.medication.dto.*;
import com.ssginc8.docto.medication.entity.*;
import com.ssginc8.docto.medication.provider.MedicationProvider;
import com.ssginc8.docto.medication.repo.*;

import lombok.RequiredArgsConstructor;

/**
 * MedicationService 구현체
 *
 * 복약 시간 등록, 수정, 삭제 / 복약 완료 처리 / 기록 및 스케줄 조회 등
 */
@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

	private final MedicationInformationRepo medicationInformationRepo;
	private final MedicationAlertTimeRepo medicationAlertTimeRepo;
	private final MedicationAlertDayRepo medicationAlertDayRepo;
	private final MedicationLogRepo medicationLogRepo;
	private final MedicationProvider medicationProvider;

	/**
	 * 약 복용 시간 등록
	 * - MedicationInformation(약 정보) 저장
	 * - MedicationAlertTime(복약 시간) 저장
	 * - MedicationAlertDay(복약 요일) 저장
	 */
	@Override
	@Transactional
	public void registerMedicationSchedule(MedicationScheduleRequest request) {
		// 약 정보 저장
		MedicationInformation info = MedicationInformation.create(
			request.getPatientGuardianId(),
			request.getMedicationName()
		);
		medicationInformationRepo.save(info);

		// 복약 시간 저장
		MedicationAlertTime alertTime = MedicationAlertTime.create(info, request.getTimeToTake());
		medicationAlertTimeRepo.save(alertTime);

		// 복약 요일 저장
		List<MedicationAlertDay> alertDays = request.getDays().stream()
			.map(day -> MedicationAlertDay.create(alertTime, day))
			.collect(Collectors.toList());
		medicationAlertDayRepo.saveAll(alertDays);
	}

	/**
	 * 사용자(userId)의 복약 기록 조회
	 * - 환자가 가진 약들의 복약 로그 전체 조회 후 DTO로 변환
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MedicationLogResponse> getMedicationLogsByUser(Long userId) {
		// 환자의 약 정보 조회 (guardian 미완성으로 userId → patientGuardianId)
		List<MedicationInformation> infos = medicationInformationRepo.findByPatientGuardianId(userId);

		// 약마다 복약 로그를 조회하여 DTO로 변환
		return infos.stream()
			.flatMap(info -> medicationLogRepo.findByMedication(info).stream())
			.map(MedicationLogResponse::from)
			.collect(Collectors.toList());
	}

	/**
	 * 사용자(userId)의 복약 스케줄 조회
	 * - 약 정보 + 복약 시간 + 복약 요일로 구성된 스케줄 리스트 반환
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MedicationScheduleResponse> getMedicationSchedulesByUser(Long userId) {
		List<MedicationInformation> infos = medicationInformationRepo.findByPatientGuardianId(userId);

		return infos.stream().flatMap(info ->
			medicationAlertTimeRepo.findByMedication(info).stream().map(alertTime -> {
				List<DayOfWeek> days = medicationAlertDayRepo.findByMedicationAlertTime(alertTime).stream()
					.map(MedicationAlertDay::getDayOfWeek)
					.collect(Collectors.toList());
				return MedicationScheduleResponse.from(info, alertTime, days);
			})
		).collect(Collectors.toList());
	}

	/**
	 * 복약 완료 처리
	 * - 상태(TAKEN/MISSED)를 기록하는 복약 로그 저장
	 * - 약에 연결된 모든 복약 시간(alertTime)마다 로그 생성
	 */
	@Override
	@Transactional
	public void completeMedication(Long medicationId, MedicationCompleteRequest request) {
		MedicationInformation info = medicationProvider.getMedication(medicationId);

		List<MedicationAlertTime> alertTimes = medicationAlertTimeRepo.findByMedication(info);
		for (MedicationAlertTime alertTime : alertTimes) {
			MedicationLog log = MedicationLog.create(
				alertTime,
				info,
				request.getStatus(),
				alertTime.getTimeToTake()
			);
			medicationLogRepo.save(log);
		}
	}

	/**
	 * 복약 시간 변경
	 * - 현재는 alertTime이 1개만 존재한다고 가정하고 첫 번째 시간만 수정
	 */
	@Override
	@Transactional
	public void updateMedicationTime(Long medicationId, MedicationUpdateRequest request) {
		List<MedicationAlertTime> alertTimes = medicationAlertTimeRepo.findByMedication(
			medicationProvider.getMedication(medicationId)
		);

		if (!alertTimes.isEmpty()) {
			alertTimes.get(0).updateTimeToTake(request.getNewTimeToTake());
		}
	}

	/**
	 * 복약 스케줄 삭제
	 * - 연결된 복약 시간(alertTime)과 복약 요일(alertDay) 전부 삭제
	 * - 약 정보도 함께 삭제
	 */
	@Override
	@Transactional
	public void deleteMedicationSchedule(Long medicationId) {
		MedicationInformation medication = medicationProvider.getMedication(medicationId);

		// 관련 로그 먼저 삭제
		List<MedicationLog> logs = medicationLogRepo.findByMedication(medication);
		medicationLogRepo.deleteAll(logs);

		// 복약 시간 + 요일 삭제
		List<MedicationAlertTime> alertTimes = medicationAlertTimeRepo.findByMedication(medication);
		for (MedicationAlertTime alertTime : alertTimes) {
			medicationAlertDayRepo.deleteByMedicationAlertTime(alertTime);
		}
		medicationAlertTimeRepo.deleteAll(alertTimes);

		// 마지막으로 약 정보 삭제
		medicationInformationRepo.delete(medication);
	}
}
