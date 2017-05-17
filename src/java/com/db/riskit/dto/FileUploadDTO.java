package com.db.riskit.dto;

import java.io.File;

import com.tree.utils.common.StringUtils;

public class FileUploadDTO {

	private String fileType;
	private String message;
	private File file;
	private String uploadFile;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		if (StringUtils.isStringNull(message)) {
			this.message = null;
		} else {
			this.message = message;
		}
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		if (StringUtils.isStringNull(fileType)) {
			this.fileType = null;
		} else {
			this.fileType = fileType;
		}
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}
	

}
