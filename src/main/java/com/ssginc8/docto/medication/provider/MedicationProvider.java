package com.ssginc8.docto.medication.provider;

import com.ssginc8.docto.medication.entity.*;
import com.ssginc8.docto.medication.repo.MedicationAlertDayRepo;
import com.ssginc8.docto.medication.repo.MedicationAlertTimeRepo;
import com.ssginc8.docto.medication.repo.MedicationInformationRepo;
import com.ssginc8.docto.medication.repo.MedicationLogRepo;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import com.ssginc8.docto.global.error.exception.medicationException.MedicationNotFoundException;
import com.ssginc8.docto.user.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicationProvider {

	private final MedicationInformationRepo medicationInformationRepo;
	private final MedicationAlertDayRepo medicationAlertDayRepo;
	private final MedicationLogRepo medicationLogRepo;
	private final UserProvider userProvider;
	private final UserServiceImpl userService;

	@Transactional(readOnly = true)
	public Page<MedicationLog> getMedicationLogsByCurrentUser(Pageable pageable) {
		User currentUser = userService.getUserFromUuid();
		return medicationLogRepo.findByMedication_User_UserIdAndDeletedAtIsNull(currentUser.getUserId(), pageable);
	}

	@Transactional(readOnly = true)
	public User getUser(Long userId) {
		return userProvider.getUserById(userId);
	}

	@Transactional(readOnly = true)
	public List<MedicationInformation> getMedicationsByUser(User user) {
		return medicationInformationRepo.findByUserAndDeletedAtIsNull(user);
	}

	@Transactional
	public void saveMedicationInformation(MedicationInformation medicationInformation) {
		medicationInformationRepo.save(medicationInformation);
	}

	@Transactional(readOnly = true)
	public User getCurrentUserFromToken() {
		return userService.getUserFromUuid();
	}

	@Transactional(readOnly = true)
	public MedicationInformation getMedication(Long medicationId) {
		return medicationInformationRepo.findByMedicationIdAndDeletedAtIsNull(medicationId)
			.orElseThrow(MedicationNotFoundException::new);
	}

	@Transactional
	public void saveMedicationLog(MedicationLog medicationLog) {
		medicationLogRepo.save(medicationLog);
	}

	@Transactional(readOnly = true)
	public List<MedicationAlertTime> findAlertTimesDayAndTime(DayOfWeek day, LocalTime now) {
		return medicationAlertDayRepo.findAlertTimesByDayAndTime(day, now);
	}

	@Transactional(readOnly = true)
	public boolean existsMedicationLog(MedicationAlertTime alertTime, LocalDate date) {
		return medicationLogRepo.existsByAlertTimeAndDate(alertTime, date);
	}

	@Transactional
	public void updateDateRange(Long medicationId, LocalDate newStart, LocalDate newEnd) {
		int updated = medicationInformationRepo.updateDateRange(medicationId, newStart, newEnd);
		if (updated == 0) {
			throw new com.ssginc8.docto.global.error.exception.medicationException.MedicationNotFoundException();
		}
		// 필요 시 영속성 컨텍스트를 clear하거나, Service에서 재조회
	}

}
