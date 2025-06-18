package com.ssginc8.docto.medication.service;

import com.ssginc8.docto.medication.dto.*;
import com.ssginc8.docto.medication.entity.MedicationInformation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicationService {
	Page<MedicationLogResponse> getMedicationLogsByCurrentUser(Pageable pageable);

	void registerMedicationSchedule(MedicationScheduleRequest request);

	List<MedicationScheduleResponse> getMedicationSchedulesByCurrentUser();

	void updateMedicationTime(Long medicationId, MedicationUpdateRequest request);

	void deleteMedicationSchedule(Long medicationId);

	void completeMedication(Long medicationId, MedicationCompleteRequest request);

	MedicationScheduleResponse getMedicationScheduleById(Long medicationId);
}
