package com.ssginc8.docto.file.service;

import com.ssginc8.docto.file.dto.UploadFile;

public interface FileService {
	public UploadFile.Response uploadImage(UploadFile.Request request);
}
