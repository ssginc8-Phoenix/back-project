package com.ssginc8.docto.file.service;

import java.io.IOException;

import com.ssginc8.docto.file.dto.UploadFile;
import com.ssginc8.docto.file.entity.File;

public interface FileService {
	public File uploadImage(UploadFile.Request request) throws IOException;
}
