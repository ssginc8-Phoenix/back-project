package com.ssginc8.docto.medication.repo;

import com.ssginc8.docto.medication.entity.MedicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationLogRepo extends JpaRepository<MedicationLog, Long> {
	Page<MedicationLog> findByMedication_User_UserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
}
