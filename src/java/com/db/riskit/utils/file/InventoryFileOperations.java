package com.db.riskit.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.InventoryDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;

public class InventoryFileOperations implements RiskItConstants {

	private static final InventoryFileOperations _instance = new InventoryFileOperations();
	private File inventoryFile;

	private InventoryFileOperations() 
	{
		try 
		{
			inventoryFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_INVENTORY);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static InventoryFileOperations getInstance() {
		return _instance;
	}

	private void close(Object wb) 
	{
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

	public ArrayList<InventoryDTO> getInventoryList(int sheetNo) 
	{
		Workbook inventoryWB = null;
		Sheet inventorySheet = null;
		ArrayList<InventoryDTO> ivDTOList = null;
		InventoryDTO ivDTO = null;
		synchronized (inventoryFile) 
		{
			try 
			{
				inventoryWB = Workbook.getWorkbook(inventoryFile);
				inventorySheet = inventoryWB.getSheet(sheetNo);
				ivDTOList = new ArrayList<InventoryDTO>();
				int i=0;
				if (inventorySheet != null) {
					for (int counter = 0; counter < inventorySheet.getRows(); counter++) 
					{
						ivDTO = new InventoryDTO();
						ivDTO.setSno(counter);
						ivDTO.setConfigName(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setEnv(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setRegion(inventorySheet.getCell(i++, counter).getContents());						
						ivDTO.setReVersion(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setJsm(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setCacheTrades(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setDbName(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setDefaultDBAX(inventorySheet.getCell(i++, counter).getContents());
						ivDTO.setDbaxParserVersion(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setCcInstance(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setCcVersion(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setJsmInstance(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setJsmVersion(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setTdcInstance(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setTdcVersion(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setJsmSchema(inventorySheet.getCell(i++,counter).getContents());
						ivDTO.setJsmSchemaVersion(inventorySheet.getCell(i++,counter).getContents());
						
						i=0; // Reset Countor
						ivDTOList.add(ivDTO);						
					}
				}
				return ivDTOList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(inventorySheet);
				close(inventoryWB);
			}
		}
		return null;
	}

	public Map<String, InventoryDTO> getInventoryMap(int sheetNo, String region, String env) 
	{
		Map<String, InventoryDTO> ivMap = new HashMap<String, InventoryDTO>();
		ArrayList<InventoryDTO> ivDTOList = null;
		ivDTOList = getInventoryList(sheetNo);
		if (ivDTOList == null || ivDTOList.isEmpty()) 
		{
			return null;
		} 
		else 
		{
			for (InventoryDTO ivDTO : getInventoryList(sheetNo)) 
			{
				if (region.equalsIgnoreCase(ivDTO.getRegion().trim()) && env.equalsIgnoreCase(ivDTO.getEnv().trim())) 
				{
					ivMap.put(ivDTO.getConfigName(), ivDTO);
				}
			}
		}
		return ivMap;
	}

	public boolean postInventory(int sheetNo, String region, String env, Map<String, InventoryDTO> ivRequestMap) 
	{
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		InventoryDTO ivDTO = null;
		Map<String, InventoryDTO> ivMap = null;
		Iterator<String> iterator = null;
		String config = null;
		ivMap = getInventoryMap(sheetNo, region, env);
		int nextRow;
		synchronized (inventoryFile) 
		{
			try 
			{
				workbook = Workbook.getWorkbook(inventoryFile);
				copy = Workbook.createWorkbook(inventoryFile, workbook);
				sheet = copy.getSheet(sheetNo);
				nextRow = sheet.getRows();				
				iterator = ivRequestMap.keySet().iterator();
				int i=0;
				while (iterator.hasNext()) 
				{
					config = iterator.next().toString();
					ivDTO = ivRequestMap.get(config);					
					if (ivMap != null && !ivMap.isEmpty()) 
					{
						InventoryDTO tempDTO = ivMap.get(config);
						if (tempDTO != null) {
							nextRow = tempDTO.getSno();
							ivMap.remove(config);
						} else {
							nextRow = sheet.getRows();
						}						
					} else {
						nextRow = sheet.getRows();
					}
					Logger.log(this.getClass().getName(), Logger.INFO, "Record to be added after row : " + nextRow);
					sheet.addCell(new Label(i++, nextRow, ivDTO.getConfigName()));					
					sheet.addCell(new Label(i++, nextRow, env));
					sheet.addCell(new Label(i++, nextRow, region));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getReVersion()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getJsm()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getCacheTrades()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getDbName()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getDefaultDBAX()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getDbaxParserVersion()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getCcInstance()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getCcVersion()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getJsmInstance()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getJsmVersion()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getTdcInstance()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getTdcVersion()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getJsmSchema()));
					sheet.addCell(new Label(i++, nextRow, ivDTO.getJsmSchemaVersion()));
					i=0; // Reset Countor
				}
				if (ivMap != null && !ivMap.isEmpty()) {
					    Iterator<Entry<String, InventoryDTO>> it = ivMap.entrySet().iterator();
					    while (it.hasNext()) {
					        Map.Entry<String, InventoryDTO> pairs = (Map.Entry<String, InventoryDTO>)it.next();
					        InventoryDTO tempIvDTO = (InventoryDTO) pairs.getValue();
					        Logger.log(this.getClass().getName(), Logger.INFO, "Deleting Config: " + pairs.getKey()+", "+tempIvDTO);
					        sheet.removeRow(tempIvDTO.getSno());
					    }
				}
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
	
	public boolean JCTInventory(int sheetNo, String region, String env, Map<String, InventoryDTO> ivRequestMap) 
	{
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		InventoryDTO ivDTO = null;
		Map<String, InventoryDTO> ivMap = null;
		Iterator<String> iterator = null;
		String config = null;
		ivMap = getInventoryMap(sheetNo, region, env);
		int nextRow;
		synchronized (inventoryFile) 
		{
			try 
			{
				workbook = Workbook.getWorkbook(inventoryFile);
				copy = Workbook.createWorkbook(inventoryFile, workbook);
				sheet = copy.getSheet(sheetNo);
				nextRow = sheet.getRows();				
				iterator = ivRequestMap.keySet().iterator();
				int i=9; // CC Instance Column Number
				while (iterator.hasNext()) 
				{
					config = iterator.next().toString();
					ivDTO = ivRequestMap.get(config);					
					if (ivMap != null && !ivMap.isEmpty()) 
					{
						InventoryDTO tempDTO = ivMap.get(config);
						if (tempDTO != null) {
							nextRow = tempDTO.getSno();
							Logger.log(this.getClass().getName(), Logger.INFO, "Record to be added after row : " + nextRow);
							sheet.addCell(new Label(i++, nextRow, ivDTO.getCcInstance()));
							sheet.addCell(new Label(i++, nextRow, ivDTO.getCcVersion()));
							sheet.addCell(new Label(i++, nextRow, ivDTO.getJsmInstance()));
							sheet.addCell(new Label(i++, nextRow, ivDTO.getJsmVersion()));
							sheet.addCell(new Label(i++, nextRow, ivDTO.getTdcInstance()));
							sheet.addCell(new Label(i++, nextRow, ivDTO.getTdcVersion()));
							i=9; // Reset Counter to CC Instance Column Number
						} 					
					} 					
				}				
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
}
