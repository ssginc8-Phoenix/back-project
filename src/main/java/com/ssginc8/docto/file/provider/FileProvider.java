package com.ssginc8.docto.file.provider;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.repository.FileRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
public class FileProvider {
	private final FileRepo fileRepo;

	public File saveFile(File file) {
		return fileRepo.save(file);
	}


	public List<String> getFileUrlsByIds(List<Long> fileIds) {
		if (fileIds == null || fileIds.isEmpty()) {
			return Collections.emptyList();
		}
		// 한 번에 IN 조회
		return fileRepo.getFileUrlsByIds(fileIds)
			.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	public File getFileById(Long fileId) {
		return fileRepo.findById(fileId)
			.orElseThrow(() -> new EntityNotFoundException("File not found: " + fileId));
	}

	public List<File> findAllById(List<Long> fileIds) {
		if (fileIds == null || fileIds.isEmpty()) {
			return Collections.emptyList();
		}
		return fileRepo.findAllById(fileIds);
	}

	@Transactional
	public void deleteFileById(Long fileId) {
		fileRepo.deleteById(fileId);
	}
}
