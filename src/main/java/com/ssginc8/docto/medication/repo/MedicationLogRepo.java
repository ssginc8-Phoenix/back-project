package com.ssginc8.docto.medication.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicationLogRepo extends JpaRepository<MedicationLog, Long> {
	Page<MedicationLog> findByMedication_User_UserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

	@Query("""
		SELECT COUNT(m) > 0
		FROM MedicationLog m
		WHERE m.medicationAlertTime = :alertTime
			AND DATE(m.timeToTake) = :date
	""")
	boolean existsByAlertTimeAndDate(
		@Param("alertTime")MedicationAlertTime alertTime,
		@Param("date")LocalDate date
	);
}
