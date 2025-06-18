package com.ssginc8.docto.hospital.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.hospital.entity.Hospital;

public interface HospitalRepo extends JpaRepository<Hospital, Long> {
	@Query(value = """
    SELECT h.*, (
        6371 * acos(
            cos(radians(:lat)) * cos(radians(h.latitude)) *
            cos(radians(h.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(h.latitude))
        )
    ) AS distance
    FROM tbl_hospital h
    WHERE (
        6371 * acos(
            cos(radians(:lat)) * cos(radians(h.latitude)) *
            cos(radians(h.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(h.latitude))
        )
    ) <= :radius
    AND (:query IS NULL OR h.name LIKE CONCAT('%', :query, '%'))
    """,
		countQuery = """
    SELECT count(*) FROM tbl_hospital h
    WHERE (
        6371 * acos(
            cos(radians(:lat)) * cos(radians(h.latitude)) *
            cos(radians(h.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(h.latitude))
        )
    ) <= :radius
    AND (:query IS NULL OR h.name LIKE CONCAT('%', :query, '%'))
    """,
		nativeQuery = true)
	Page<Hospital> findHospitalsWithinRadius(
		@Param("lat") double lat,
		@Param("lng") double lng,
		@Param("radius") double radius,
		@Param("query") String query,
		Pageable pageable
	);

	Page<Hospital> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<Hospital> findByHospitalIdAndDeletedAtIsNull(Long hospitalId);

	@Query("""
    SELECT h FROM Hospital h
    WHERE h.deletedAt IS NULL
    AND (:query IS NOT NULL AND h.name LIKE %:query%)
    ORDER BY h.name ASC
""")
	Page<Hospital> searchHospitalsWithoutLocation(
		@Param("query") String query,
		Pageable pageable
	);

	Optional<Hospital> findByUserUserId(Long userId);

	List<Hospital> findTop5BySpecializationOrderByWaitingAsc(Specialization specialization);

}
