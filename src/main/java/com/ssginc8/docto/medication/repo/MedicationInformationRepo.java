package com.ssginc8.docto.medication.repo;

import com.ssginc8.docto.medication.entity.MedicationInformation;
import com.ssginc8.docto.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicationInformationRepo extends JpaRepository<MedicationInformation, Long> {
	Optional<MedicationInformation> findByMedicationIdAndDeletedAtIsNull(Long medicationId);

	List<MedicationInformation> findByUserAndDeletedAtIsNull(User user);
}
