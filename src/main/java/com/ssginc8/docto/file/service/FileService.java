package com.ssginc8.docto.file.service;

import com.ssginc8.docto.file.service.dto.UpdateFile;
import com.ssginc8.docto.file.service.dto.UploadFile;

public interface FileService {
	UploadFile.Result uploadImage(UploadFile.Command command);

	void deleteFile(String fileName);

	UpdateFile.Result updateFile(UpdateFile.Command command);
}
