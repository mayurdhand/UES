package com.db.riskit.actions.admin;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.CCDTO;
import com.db.riskit.utils.CCClient;
import com.db.riskit.utils.file.CCFileOperations;
import com.opensymphony.xwork2.ModelDriven;

public class CCAction extends RiskItActionSupport implements ModelDriven<CCDTO>,RiskItConstants {
	
	private static final long serialVersionUID = 1L;
	private CCDTO ccDTO;
	private List<CCDTO> ccDTOList;
	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public List<CCDTO> getCcDTOList() {
		return ccDTOList;
	}

	public void setCcDTOList(List<CCDTO> ccDTOList) {
		this.ccDTOList = ccDTOList;
	}

	public String execute() {
		return SUCCESS;
	}

	public CCDTO getModel() {
		return this.ccDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareCCDTO();
	}

	private void prepareCCDTO() {
		if (this.ccDTO == null)
			this.ccDTO = new CCDTO();
	}
	
	public String reloadCache() {
		CCClient ccClient = new CCClient();
		ccClient.reloadCache(getModel());
		return "cc";
	}	
	
	public String addConfig() {		
		int status = CCFileOperations.getInstance().addConfig(getModel());
		if(status == REQUEST_SUCCESS) {
			setSuccess(true);
			getModel().setMessage("Config added successfully");
		} else if(status == REQUEST_DUPLICATE_ENTRY){
			getModel().setMessage("Config already exists.");
		}
		return "cc";
	}

	public String updateConfig() {		
		boolean isSuccess = CCFileOperations.getInstance().updateConfig(getModel());
		if (isSuccess) {
			/* 
			 * No need to setSuccess(true) as this method is called by ajax and
			 * will will execute success case unless an exception is thrown 
			 */
			getModel().setMessage("Config updated successfully");			
		} else {
			getModel().setMessage("Could not update contact. Please try again later.");
		}
		return "cc";
	}

	public String deleteConfig() {
		boolean isSuccess = CCFileOperations.getInstance().deleteConfig(ccDTO);
		if (isSuccess) {
			/* 
			 * No need to setSuccess(true) as this method is called by ajax and
			 * will will execute success case unless an exception is thrown 
			 */
			getModel().setMessage("Config deleted successfully.");
		} else {
			getModel().setMessage("Could not delete config. Please try again later.");
		}
		return "cc";
	}

}
