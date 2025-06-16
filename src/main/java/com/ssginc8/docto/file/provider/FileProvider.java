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

	/**
	 * 파일 ID로 S3 URL을 조회합니다.
	 * @param fileId tbl_file PK
	 * @return URL 문자열 또는 null
	 */
	public String getFileUrlById(Long fileId) {
		if (fileId == null) {
			return null;
		}
		// repository에 선언된 getFileUrlById(@Param("fileId") Long) 호출
		return fileRepo.getFileUrlById(fileId);
	}
}
