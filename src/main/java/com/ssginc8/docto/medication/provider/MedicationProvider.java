package com.ssginc8.docto.medication.provider;

import com.ssginc8.docto.medication.entity.*;
import com.ssginc8.docto.medication.repo.MedicationInformationRepo;
import com.ssginc8.docto.medication.repo.MedicationLogRepo;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import com.ssginc8.docto.global.error.exception.medicationException.MedicationNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicationProvider {

	private final MedicationInformationRepo medicationInformationRepo;
	private final MedicationLogRepo medicationLogRepo;
	private final UserProvider userProvider;

	@Transactional(readOnly = true)
	public Page<MedicationLog> getMedicationLogsByCurrentUser(Pageable pageable) {
		User currentUser = userProvider.getCurrentUserFromToken();
		return medicationLogRepo.findByMedication_User_UserIdAndDeletedAtIsNull(currentUser.getUserId(), pageable);

		return null;
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
		return userProvider.getCurrentUserFromToken();
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

}
