// src/main/java/com/ssginc8/docto/insurance/service/dto/FileDownload.java
package com.ssginc8.docto.insurance.service.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * S3에서 읽어온 파일 바이트와 메타정보를 담는 DTO
 */
@Getter
@Builder
public class FileDownload {
	/** 실제 파일 바이트 배열 */
	private final byte[] data;
	/** 파일 크기(바이트) */
	private final long size;
	/** MIME 타입 */
	private final String contentType;
	/** 원본 파일명 */
	private final String originalName;
}
