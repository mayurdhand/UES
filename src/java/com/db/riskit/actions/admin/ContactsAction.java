package com.db.riskit.actions.admin;

import java.util.List;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.ContactsDTO;
import com.db.riskit.utils.file.CLFileOperations;
import com.opensymphony.xwork2.ModelDriven;

public class ContactsAction extends RiskItActionSupport implements
		ModelDriven<ContactsDTO>,RiskItConstants {
	
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
	
	public String addContact() {		
		int status = CLFileOperations.getInstance().addContact(getModel());
		if(status == REQUEST_SUCCESS) {
			setSuccess(true);
			getModel().setMessage("Contact added successfully");
		} else if(status == REQUEST_DUPLICATE_ENTRY){
			setSuccess(true);
			getModel().setMessage("Team already exists.");
		}
		return "contacts";
	}

	public String updateContact() {		
		boolean isSuccess = CLFileOperations.getInstance().updateContact(getModel());
		if (isSuccess) {
			/* 
			 * No need to setSuccess(true) as this method is called by ajax and
			 * will will execute success case unless an exception is thrown 
			 */
			getModel().setMessage("Contact updated successfully");			
		} else {
			getModel().setMessage("Could not update contact. Please try again later.");
		}
		return "contacts";
	}

	public String deleteContact() {
		boolean isSuccess = CLFileOperations.getInstance().deleteContact(contactsDTO);
		if (isSuccess) {
			/* 
			 * No need to setSuccess(true) as this method is called by ajax and
			 * will will execute success case unless an exception is thrown 
			 */
			getModel().setMessage("Contact deleted successfully.");
		} else {
			getModel().setMessage("Could not delete contact. Please try again later.");
		}
		return "contacts";
	}

}
