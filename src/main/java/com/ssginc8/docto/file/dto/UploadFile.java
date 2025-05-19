package com.ssginc8.docto.file.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.file.entity.Category;

import lombok.Builder;
import lombok.Getter;

public class UploadFile {
	@Getter
	public static class Request {
		private MultipartFile file;
		private Category category;

		@Builder
		public Request(MultipartFile file, Category category) {
			this.file = file;
			this.category = category;
		}
	}

	@Getter
	public static class Response {
		private Category category;
		private String fileName;
		private String originalFileName;
		private String url;
		private String bucket;
		private Long fileSize;
		private String fileType;

		@Builder
		public Response(Category category, String fileName, String originalFileName, String url, String bucket,
			Long fileSize, String fileType) {
			this.category = category;
			this.fileName = fileName;
			this.originalFileName = originalFileName;
			this.url = url;
			this.bucket = bucket;
			this.fileSize = fileSize;
			this.fileType = fileType;
		}
	}
}
