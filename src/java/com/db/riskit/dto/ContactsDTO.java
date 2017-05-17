package com.db.riskit.dto;

import com.tree.utils.common.StringUtils;

public class ContactsDTO {
	private int teamId;
	private String team;
	private String emailId;
	private String contactNumber;
	private String primaryEscalation;
	private String secondryEscalation;
	private String message;
	
	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}	

	public String getPrimaryEscalation() {
		return primaryEscalation;
	}

	public void setPrimaryEscalation(String primaryEscalation) {
		if (StringUtils.isStringNull(primaryEscalation)) {
			this.primaryEscalation = null;
		} else {
			this.primaryEscalation = primaryEscalation;
		}
	}

	public String getSecondryEscalation() {
		return secondryEscalation;
	}

	public void setSecondryEscalation(String secondryEscalation) {
		if (StringUtils.isStringNull(secondryEscalation)) {
			this.secondryEscalation = null;
		} else {
			this.secondryEscalation = secondryEscalation;
		}
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		if (StringUtils.isStringNull(team)) {
			this.team = null;
		} else {
			this.team = team;
		}
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		if (StringUtils.isStringNull(emailId)) {
			this.emailId = null;
		} else {
			this.emailId = emailId;
		}
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		if (StringUtils.isStringNull(contactNumber)) {
			this.contactNumber = null;
		} else {
			this.contactNumber = contactNumber;
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		if (StringUtils.isStringNull(message)) {
			this.message = null;
		} else {
			this.message = message;
		}
	}

	@Override
	public String toString() {
		return "ContactsDTO [teamId=" + teamId + ", team=" + team
				+ ", emailId=" + emailId + ", contactNumber=" + contactNumber
				+ ", primaryEscalation=" + primaryEscalation
				+ ", secondryEscalation=" + secondryEscalation + ", message="
				+ message + "]";
	}

}
