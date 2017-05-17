package com.db.riskit.actions.noauth;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.ContactsDTO;
import com.db.riskit.utils.file.CLFileOperations;
import com.opensymphony.xwork2.ModelDriven;

public class ContactsAction extends RiskItActionSupport implements
		ModelDriven<ContactsDTO> {
	private static final long serialVersionUID = 1L;
	private ContactsDTO contactsDTO;
	private List<ContactsDTO> contactsDTOList;
	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<ContactsDTO> getContactsDTOList() {
		return contactsDTOList;
	}

	public void setContactsDTOList(List<ContactsDTO> contactsDTOList) {
		this.contactsDTOList = contactsDTOList;
	}

	public String execute() {
		return SUCCESS;
	}

	public ContactsDTO getModel() {
		return this.contactsDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareContactsDTO();
	}

	private void prepareContactsDTO() {
		if (this.contactsDTO == null)
			this.contactsDTO = new ContactsDTO();
	}

	public String showContacts() {
		List<ContactsDTO> contactsDTOList = null;
		contactsDTOList = CLFileOperations.getInstance().getContactList();
		if (contactsDTOList != null && contactsDTOList.size() == 0) {
			setContactsDTOList(null);
		} else {
			setContactsDTOList(contactsDTOList);
		}
		setSuccess(true);
		return "contacts";
	}	

}
