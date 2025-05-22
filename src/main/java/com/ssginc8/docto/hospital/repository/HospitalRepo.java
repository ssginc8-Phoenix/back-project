package com.ssginc8.docto.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssginc8.docto.hospital.entity.Hospital;

public interface HospitalRepo extends JpaRepository<Hospital, Long >{

	@Query(value = """
        SELECT *,
               (6371 * acos(
                    cos(radians(:userLat)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:userLng)) +
                    sin(radians(:userLat)) * sin(radians(latitude))
                )) AS distance
        FROM tbl_hospital
        WHERE latitude IS NOT NULL AND longitude IS NOT NULL
          AND (6371 * acos(
                cos(radians(:userLat)) * cos(radians(latitude)) *
                cos(radians(longitude) - radians(:userLng)) +
                sin(radians(:userLat)) * sin(radians(latitude))
          )) <= :radius
        ORDER BY distance ASC
        """,
		nativeQuery = true)
	List<Hospital> findHospitalsWithinRadius(
		@Param("userLat") double userLat,
		@Param("userLng") double userLng,
		@Param("radius") double radius);
}
