package com.ssginc8.docto.medication.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;

public interface MedicationAlertTimeRepo extends JpaRepository<MedicationAlertTime, Long> {

	Optional<MedicationAlertTime> findByMedicationAlertTimeIdAndDeletedAtIsNull(Long medicationAlertTimeId);

	List<MedicationAlertTime> findByMedicationAndDeletedAtIsNull(MedicationInformation medication);
}
