package com.db.riskit.actions.noauth;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.InventoryDTO;
import com.db.riskit.utils.file.InventoryFileOperations;
import com.db.riskit.utils.logging.Logger;
import com.opensymphony.xwork2.ModelDriven;

public class InventoryAction extends RiskItActionSupport implements
		ModelDriven<InventoryDTO> {
	private static final long serialVersionUID = 1L;
	private InventoryDTO inventoryDTO;
	private List<InventoryDTO> inventoryDTOList;
	private InputStream inputStream;
	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<InventoryDTO> getInventoryDTOList() {
		return inventoryDTOList;
	}

	public void setInventoryDTOList(List<InventoryDTO> inventoryDTOList) {
		this.inventoryDTOList = inventoryDTOList;
	}

	public String execute() {
		return SUCCESS;
	}

	public InventoryDTO getModel() {
		return this.inventoryDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareInventoryDTO();
	}

	private void prepareInventoryDTO() {
		if (this.inventoryDTO == null)
			this.inventoryDTO = new InventoryDTO();
	}	

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	private void viewInventory(int sheetNo) {
		List<InventoryDTO> inDTOList = null;

		try {
			inDTOList = InventoryFileOperations.getInstance().getInventoryList(sheetNo);
			if (inDTOList.size() == 0) {
				setInventoryDTOList(null);				
			} else {
				setInventoryDTOList(inDTOList);
			}
			setSuccess(true);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public String viewLondonInventory() {
		viewInventory(0);
		return "viewInventory";
	}

	public String viewNYInventory() {
		viewInventory(1);
		return "viewInventory";
	}

	public String viewASPACInventory() {
		viewInventory(2);
		return "viewInventory";
	}
	
	public String downloadFile(int sheetNo) throws Exception {
		ArrayList<InventoryDTO> ivDtoList = null;	
		ivDtoList = InventoryFileOperations.getInstance().getInventoryList(sheetNo);
		StringBuffer sb = new StringBuffer();
		sb.append("CONFIG NAME,");
		sb.append("ENVIRONMENT,");
		sb.append("REGIION,");
		sb.append("RE VERSION,");
		sb.append("JSM,");
		sb.append("TDC,");
		sb.append("DATABASE,");
		sb.append("DEFAULT DBAX,");
		sb.append("DBAX PARSER VERSION,");
		sb.append("CC INSTANCE,");
		sb.append("CC VERSION,");
		sb.append("JSM INSTANCE,");
		sb.append("JSM VERSION,");
		sb.append("JSM SCHEMA,");
		sb.append("JSM SCHEMA VERSION,");
		sb.append("TDC INSTANCE,");
		sb.append("TDC  VERSION");		
		sb.append("\n");
		for (InventoryDTO temp : ivDtoList) {
			sb.append("\""+temp.getConfigName() + "\",");
			sb.append("\""+temp.getEnv() + "\",");
			sb.append("\""+temp.getRegion() + "\",");
			sb.append("\""+temp.getReVersion() + "\",");
			sb.append("\""+temp.getJsm() + "\",");
			sb.append("\""+temp.getCacheTrades() + "\",");
			sb.append("\""+temp.getDbName() + "\",");
			sb.append("\""+temp.getDefaultDBAX() + "\",");
			sb.append("\""+temp.getDbaxParserVersion() + "\",");
			sb.append("\""+temp.getCcInstance() + "\",");
			sb.append("\""+temp.getCcVersion() + "\",");
			sb.append("\""+temp.getJsmInstance() + "\",");
			sb.append("\""+temp.getJsmVersion() + "\",");
			sb.append("\""+temp.getJsmSchema() + "\",");
			sb.append("\""+temp.getJsmSchemaVersion() + "\",");
			sb.append("\""+temp.getTdcInstance() + "\",");
			sb.append("\""+temp.getTdcVersion() + "\"");			
			sb.append("\n");
		}
		setInputStream(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
		return "downloadFile";
	}	
	
	public String downloadLondonInventory() {
		String returnString = null;
		try {
			returnString = downloadFile(0);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		return returnString;
	}

	public String downloadNYInventory() {
		String returnString = null;
		try {
			returnString = downloadFile(1);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		return returnString;
	}

	public String downloadASPACInventory() {
		String returnString = null;
		try {
			returnString = downloadFile(2);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		return returnString;
	}		

}
