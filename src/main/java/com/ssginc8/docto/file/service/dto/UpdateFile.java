package com.ssginc8.docto.file.service.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.file.entity.Category;

import lombok.Builder;
import lombok.Getter;

public class UpdateFile {
	@Getter
	public static class Command {
		private MultipartFile file;
		private Category category;
		private String OriginalFileName;

		@Builder
		public Command(MultipartFile file, Category category, String originalFileName) {
			this.file = file;
			this.category = category;
			OriginalFileName = originalFileName;
		}
	}

	@Getter
	public static class Result {
		private Category category;
		private String fileName;
		private String originalFileName;
		private String url;
		private String bucket;
		private Long fileSize;
		private String fileType;

		private Result(Category category, String fileName, String originalFileName, String url, String bucket,
			Long fileSize,
			String fileType) {
			this.category = category;
			this.fileName = fileName;
			this.originalFileName = originalFileName;
			this.url = url;
			this.bucket = bucket;
			this.fileSize = fileSize;
			this.fileType = fileType;
		}

		public static Result from(UploadFile.Result result) {
			return new Result(result.getCategory(), result.getFileName(), result.getOriginalFileName(), result.getUrl(),
				result.getBucket(), result.getFileSize(), result.getFileType());
		}
	}
}