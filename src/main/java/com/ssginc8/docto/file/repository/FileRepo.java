package com.ssginc8.docto.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.file.entity.File;

public interface FileRepo extends JpaRepository<File, Long> {
}
