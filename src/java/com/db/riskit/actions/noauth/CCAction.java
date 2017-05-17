package com.db.riskit.actions.noauth;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.CCDTO;
import com.db.riskit.utils.file.CCFileOperations;
import com.opensymphony.xwork2.ModelDriven;

public class CCAction extends RiskItActionSupport implements
		ModelDriven<CCDTO>,RiskItConstants {
	
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
		prepareContactsDTO();
	}

	private void prepareContactsDTO() {
		if (this.ccDTO == null)
			this.ccDTO = new CCDTO();
	}
	
	public String showCCConfigs() {
		List<CCDTO> ccDTOList = null;
		ccDTOList = CCFileOperations.getInstance().getCCConfigList();
		if (ccDTOList != null && ccDTOList.size() == 0) {
			setCcDTOList(null);
		} else {
			setCcDTOList(ccDTOList);
		}
		setSuccess(true);
		return "cc";
	}	

}
