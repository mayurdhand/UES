package com.db.riskit.actions.admin;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.LinksDTO;
import com.db.riskit.utils.file.CLFileOperations;
import com.opensymphony.xwork2.ModelDriven;

public class LinksAction extends RiskItActionSupport implements
		ModelDriven<LinksDTO>,RiskItConstants {
	private static final long serialVersionUID = 1L;
	private LinksDTO linksDTO;
	private List<LinksDTO> linksDTOList;
	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<LinksDTO> getLinksDTOList() {
		return linksDTOList;
	}

	public void setLinksDTOList(List<LinksDTO> linksDTOList) {
		this.linksDTOList = linksDTOList;
	}

	public String execute() {
		return SUCCESS;
	}

	public LinksDTO getModel() {
		return this.linksDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareLinksDTO();
	}

	private void prepareLinksDTO() {
		if (this.linksDTO == null)
			this.linksDTO = new LinksDTO();
	}	

	public String addLink() {		
		int status = CLFileOperations.getInstance().addLink(getModel());
		if (status == REQUEST_SUCCESS) {
			setSuccess(true);
			getModel().setMessage("Link added successfully.");
		} else if(status == REQUEST_DUPLICATE_ENTRY) {
			setSuccess(true);
			getModel().setMessage("Application link already exists.");
		}
		return "links";
	}
	
	public String deleteLink() {
		boolean isSuccess = CLFileOperations.getInstance().deleteLink(linksDTO);
		if (isSuccess) {
			/* 
			 * No need to setSuccess(true) as this method is called by ajax and
			 * will will execute success case unless an exception is thrown 
			 */
			getModel().setMessage("Link deleted successfully.");
		} else {
			getModel().setMessage("Could not delete link. Please try again later.");
		}				
		return "links";
	}
	
	public String updateLink() {		
		boolean isSuccess = CLFileOperations.getInstance().updateLink(getModel());
		if (isSuccess) {
			setSuccess(true);
			getModel().setMessage("Link updated successfully");
		} 	
		return "links";
	}
}
