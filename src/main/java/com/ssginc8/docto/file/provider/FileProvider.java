package com.ssginc8.docto.file.provider;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.repository.FileRepo;

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

	public File findById(Long fileId) {
		return fileRepo.findById(fileId)
			.orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
	}
}
