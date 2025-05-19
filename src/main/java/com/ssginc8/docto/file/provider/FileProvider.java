package com.ssginc8.docto.file.provider;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.repository.FileRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FileProvider {
	private final FileRepo fileRepo;

	public File saveFile(File file) {
		return fileRepo.save(file);
	}
}
