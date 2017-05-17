package com.db.riskit.actions.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.FileUploadDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.sms.platform.utilities.files.FilesUtils;
import com.tree.utils.file.FileUtils;

public class FileUploadAction extends RiskItActionSupport implements ModelDriven<FileUploadDTO> {
	private static final long serialVersionUID = 1L;
	private FileUploadDTO fileUploadDTO;
	private boolean success = false;
	private String uploadPath = PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH);
	private String backupPath = PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_BACKUP_PATH);

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String execute() {
		setSuccess(true);
		return "uploadstatus";
	}

	public FileUploadDTO getModel() {
		return this.fileUploadDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareFileUploadDTO();
	}

	private void prepareFileUploadDTO() {
		if (this.fileUploadDTO == null)
			this.fileUploadDTO = new FileUploadDTO();
	}

	private String getFileExtension(String fileName) {
		if (fileName == null || fileName.trim().length() == 0){
			return null;
		}
		StringTokenizer st;
		st = new StringTokenizer(fileName, ".");
		String fileExtension = null;
		while (st.hasMoreTokens()) {
			fileExtension = st.nextToken();
		}
		if (fileExtension == null || fileExtension.trim().length() == 0)
			return null;
		return fileExtension.toLowerCase();
	}

	public String upload() {			
		if (fileUploadDTO.getFileType().equalsIgnoreCase("Inventory")) {
			backupAndStore(FILE_INVENTORY);
		} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RE Config Booking")) {
			backupAndStore(FILE_RE_BOOKING);
		} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RE Config Booking Requests")) {
			backupAndStore(FILE_RE_REQUEST);
		} else if (fileUploadDTO.getFileType().equalsIgnoreCase("Contacts And Links")) {
			backupAndStore(FILE_CONTACTS_LINKS);
		} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RS Booking Requests")) {
			backupAndStore(FILE_RS_REQUEST);
		} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RS Booking")) {
			backupAndStore(FILE_RS_BOOKING);
		}
		response.setContentType("text/html");
		return "uploadstatus";
	}
	
	private void backupAndStore(String destinationFileName) {
		
		String fileExtension = getFileExtension(fileUploadDTO.getUploadFile());
				
		Logger.log(this.getClass().getName(), Logger.INFO,"Upload Path : " + uploadPath);
		Logger.log(this.getClass().getName(), Logger.INFO,"Backup Path : " + backupPath);
		Logger.log(this.getClass().getName(), Logger.INFO,"Uploaded File Name : " + fileUploadDTO.getUploadFile());
		Logger.log(this.getClass().getName(), Logger.INFO,"Destination File Name : " + destinationFileName);
		
		if (fileExtension != null) {
			if (!fileExtension.equalsIgnoreCase("xls")) {
				fileUploadDTO.setMessage("Please upload a .xls file.");
			} else {
				Boolean isBackupSuccess = backupFile(destinationFileName);
				Logger.log(this.getClass().getName(), Logger.INFO,"File Backup Status : " + isBackupSuccess);
				if(isBackupSuccess){
					saveTempFileToUploadDirectory(fileUploadDTO.getFile(), uploadPath + FileUtils.PATH_SEPARATOR + destinationFileName);
					setSuccess(true);
					fileUploadDTO.setMessage("File uploaded successfully.");
				}else{
					fileUploadDTO.setMessage("Could not upload file.");
				}				
			}
		} else {
			fileUploadDTO.setMessage("Please upload an apropriate file.");
		}
	}

	private boolean backupFile(String destinationFileName) {
		String backupFileName = null;
		Date da = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss-SSS");
		String backupFilePrefix = null;
		try{
			backupFilePrefix = destinationFileName.replace(".xls", "_");
			backupFileName = backupFilePrefix + sdf.format(da) + ".xls";			
			File f = new File(uploadPath + FileUtils.PATH_SEPARATOR + destinationFileName);
			return f.renameTo(new File(backupPath + FileUtils.PATH_SEPARATOR + backupFileName));
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		return false;
	}

	private void saveTempFileToUploadDirectory(File tempFile, String destinationFileWithAbsolutePath) {
		try{
			if (FileUtils.isFileOrDirectoryExist(destinationFileWithAbsolutePath)){
				new File(destinationFileWithAbsolutePath).delete();
			}
			FilesUtils.copyfile(tempFile.getAbsolutePath(), destinationFileWithAbsolutePath);
			File newFile = new File(destinationFileWithAbsolutePath);
			Logger.log(this.getClass().getName(), Logger.INFO,"File Upload Status : " + newFile.exists());
			// tempFile.deleteOnExit();
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	private InputStream fileInputStream;

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public String downloadFile() {
		try{
			if (fileUploadDTO.getFileType().equalsIgnoreCase("Inventory")) {
				setFilename(FILE_INVENTORY);
				fileInputStream = new FileInputStream(new File(uploadPath + getFilename()));				
			} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RE Config Booking")) {
				setFilename(FILE_RE_BOOKING);
				fileInputStream = new FileInputStream(new File(uploadPath + getFilename()));
			} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RE Config Booking Requests")) {
				setFilename(FILE_RE_REQUEST);
				fileInputStream = new FileInputStream(new File(uploadPath + getFilename()));
			} else if (fileUploadDTO.getFileType().equalsIgnoreCase("Contacts And Links")) {
				setFilename(FILE_CONTACTS_LINKS);
				fileInputStream = new FileInputStream(new File(uploadPath + getFilename()));
			} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RS Booking Requests")) {
				setFilename(FILE_RS_REQUEST);
				fileInputStream = new FileInputStream(new File(uploadPath + getFilename()));
			} else if (fileUploadDTO.getFileType().equalsIgnoreCase("RS Booking")) {
				setFilename(FILE_RS_BOOKING);
				fileInputStream = new FileInputStream(new File(uploadPath + getFilename()));
			}
			setSuccess(true);
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}		
		return "downloadFile";
	}

	private String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
