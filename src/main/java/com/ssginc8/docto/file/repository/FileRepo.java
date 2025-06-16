package com.ssginc8.docto.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssginc8.docto.file.entity.File;

public interface FileRepo extends JpaRepository<File, Long> {

	@Query("SELECT f.url FROM File f WHERE f.fileId = :fileId")
	String getFileUrlById(@Param("fileId") Long fileId);

	@Query("SELECT f.url FROM File f WHERE f.fileId = (SELECT h.fileId FROM Hospital h WHERE h.hospitalId = :hospitalId)")
	String getFileUrlByHospitalId(@Param("hospitalId") Long hospitalId);
}
