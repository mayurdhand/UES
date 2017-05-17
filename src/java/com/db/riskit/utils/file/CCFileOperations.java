package com.db.riskit.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.CCDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;

public class CCFileOperations implements RiskItConstants {
	private static final CCFileOperations _instance = new CCFileOperations();
	private File CCFile;

	private CCFileOperations() {
		try {
			CCFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_CC);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static CCFileOperations getInstance() {
		return _instance;
	}

	private void close(Object wb) {
		if (wb == null) {
			return;
		} else if (wb instanceof Workbook) {
			((Workbook) wb).close();
		} else if (wb instanceof WritableWorkbook) {
			try {
				((WritableWorkbook) wb).close();
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			}
		}
	}
	
	private void write(WritableWorkbook copy) 
	{
		if (copy == null) {
			return;
		}
		try {
			copy.write();
		} catch (IOException e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public List<CCDTO> getCCConfigList() {
		Workbook ccWB = null;
		List<CCDTO> ccDTOList = null;
		Sheet ccSheet = null;
		CCDTO tempCCDTO = null;
		synchronized (CCFile) {
			try {
				ccWB = Workbook.getWorkbook(CCFile);
				ccDTOList = new ArrayList<CCDTO>();
				ccSheet = ccWB.getSheet(0);
				for (int counter = 1; counter < ccSheet.getRows(); counter++) {
					String config = ccSheet.getCell(0, counter).getContents();
					if (config != "") {
						tempCCDTO = new CCDTO();
						tempCCDTO.setConfigId(counter);
						tempCCDTO.setConfig(config);
						tempCCDTO.setDatabase(ccSheet.getCell(1,counter).getContents());
						tempCCDTO.setCcHost(ccSheet.getCell(2,counter).getContents());
						tempCCDTO.setCcPort(ccSheet.getCell(3, counter).getContents());
						ccDTOList.add(tempCCDTO);
					}
				}
				return ccDTOList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(ccWB);
			}
		}
		return null;
	}

	public boolean deleteConfig(CCDTO ccDTO) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		synchronized (CCFile) {
			try {
				workbook = Workbook.getWorkbook(CCFile);
				copy = Workbook.createWorkbook(CCFile, workbook);
				sheet = copy.getSheet(0);
				sheet.removeRow(ccDTO.getConfigId());
				return true;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(workbook);
			}
		}
		return false;
	}

	public int addConfig(CCDTO ccDTO) {
		Workbook ccWB = null;
		WritableWorkbook copy = null;
		WritableSheet ccWritable = null;
		System.out.println(ccDTO);
		synchronized (CCFile) {
			try {
				ccWB = Workbook.getWorkbook(CCFile);
				copy = Workbook.createWorkbook(CCFile, ccWB);
				ccWritable = copy.getSheet(0);
				int counter = ccWritable.getRows();
				for (int i = 0, j = 1; j < ccWritable.getRows(); j++) {
					if ((ccWritable.getCell(i, j)).getContents().toString().equals(ccDTO.getConfig().toString())) {
						return REQUEST_DUPLICATE_ENTRY;
					}
				}				
				Label label = new Label(0, counter,ccDTO.getConfig());
				ccWritable.addCell(label);
				Label label1 = new Label(1, counter,ccDTO.getDatabase());
				ccWritable.addCell(label1);
				Label label2 = new Label(2, counter,ccDTO.getCcHost());
				ccWritable.addCell(label2);
				Label label3 = new Label(3, counter,ccDTO.getCcPort());
				ccWritable.addCell(label3);
				
				return REQUEST_SUCCESS;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (RowsExceededException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(ccWB);
			}
		}
		return REQUEST_FAILURE;
	}

	public boolean updateConfig(CCDTO ccDTO) {
		Workbook ccWB = null;
		WritableWorkbook copy = null;
		WritableSheet ccWritable = null;
		synchronized (CCFile) {
			try {
				ccWB = Workbook.getWorkbook(CCFile);
				copy = Workbook.createWorkbook(CCFile, ccWB);
				ccWritable = copy.getSheet(0);
				for (int i = 0, j = 1; j < ccWritable.getRows(); j++) {
					if ((ccWritable.getCell(i, j)).getContents().toString().equals(ccDTO.getConfig().toString())) {
						Label label = new Label(0, j,ccDTO.getConfig());
						ccWritable.addCell(label);
						Label label1 = new Label(1, j,ccDTO.getDatabase());
						ccWritable.addCell(label1);
						Label label2 = new Label(2, j,ccDTO.getCcHost());
						ccWritable.addCell(label2);
						Label label3 = new Label(3, j,ccDTO.getCcPort());
						ccWritable.addCell(label3);
					}
				}
				return true;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (RowsExceededException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(ccWB);
			}
		}
		return false;
	}


}
