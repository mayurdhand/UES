package com.db.riskit.actions.noauth;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.TicketDTO;
import com.db.riskit.utils.TicketUtil;
import com.db.riskit.utils.logging.Logger;
import com.opensymphony.xwork2.ModelDriven;

public class TicketAction extends RiskItActionSupport implements ModelDriven<TicketDTO> {

	private static final long serialVersionUID = 1L;
	private TicketDTO ticketDTO;
	private List<TicketDTO> ticketDTOList;
	private boolean success = false;	
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public List<TicketDTO> getTicketDTOList() {
		return ticketDTOList;
	}

	public void setTicketDTOList(List<TicketDTO> ticketDTOList) {
		this.ticketDTOList = ticketDTOList;
	}	

	public TicketDTO getModel() {
		return this.ticketDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareTicketDTO();
	}

	private void prepareTicketDTO() {
		if (this.ticketDTO == null)
			this.ticketDTO = new TicketDTO();
	}

	public String execute() {
		TicketUtil ticketUtil = new TicketUtil();
		ticketUtil.raiseINC(ticketDTO);
		Logger.log(this.getClass().getName(), Logger.INFO,ticketDTO.toString());
		if(ticketDTO.getInCode() ==null){
			setSuccess(false);
		}else{
			setSuccess(true);
		}
		return "execute";
	}
	
}
