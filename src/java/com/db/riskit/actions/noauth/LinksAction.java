package com.db.riskit.actions.noauth;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.LinksDTO;
import com.db.riskit.utils.file.CLFileOperations;
import com.opensymphony.xwork2.ModelDriven;

public class LinksAction extends RiskItActionSupport implements	ModelDriven<LinksDTO> {
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

	public String showLinks() {

		List<LinksDTO> linksDTOList = null;
		linksDTOList = CLFileOperations.getInstance().getLinksList();
		if (linksDTOList != null && linksDTOList.size() == 0) {
			setLinksDTOList(null);			
		} else {
			setLinksDTOList(linksDTOList);
		}
		setSuccess(true);
		return "links";
	}

	public void clearDTO() {
		this.linksDTO.setApplicationDesc(null);
		this.linksDTO.setApplicationName(null);
		this.linksDTO.setUrl(null);
	}

}
