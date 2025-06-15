package com.ssginc8.docto.file.provider;

import java.util.List;
import java.util.Map;

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



	public String getFileUrlById(Long fileId) {
		if (fileId == null) return null;
		return fileRepo.findById(fileId)
			.map(File::getUrl)
			.orElse(null);
	}


}
