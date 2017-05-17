package com.db.riskit.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tree.utils.common.StringUtils;

public class BookConfigDTO implements Comparable<BookConfigDTO> {
	private String envName;
	private String projectName;
	private String baContact;
	private String manager;
	private String devContact;
	private String qaContact;
	private String stakeholder;
	private String fromDate;
	private String toDate;
	private String bookedBy;
	private String bookingStatus;
	private String emailAddress;

	private Integer lineNo;
	private String region;
	private String reason;
	private String reqSummary;
	private String notifies;
	private String business;

	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	private Date fromDateInDF;
	private Date toDateInDF;
	private boolean error = false;
	private String message;

	private boolean showExpiredBookings = false;

	@Override
	public int compareTo(BookConfigDTO arg0) {
		return this.getEnvName().compareTo(arg0.getEnvName());
	}

	public boolean getShowExpiredBookings() {
		return showExpiredBookings;
	}

	public void setShowExpiredBookings(boolean showExpiredBookings) {
		this.showExpiredBookings = showExpiredBookings;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		if (StringUtils.isStringNull(reason)) {
			this.reason = null;
		} else {
			this.reason = reason;
		}
	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		if (StringUtils.isStringNull(region)) {
			this.region = null;
		} else {
			this.region = region;
		}
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		if (StringUtils.isStringNull(emailAddress)) {
			this.emailAddress = null;
		} else {
			this.emailAddress = emailAddress;
		}
	}

	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		if (StringUtils.isStringNull(bookingStatus)) {
			this.bookingStatus = null;
		} else {
			this.bookingStatus = bookingStatus;
		}
	}

	public Date getFromDateInDF() {
		return fromDateInDF;
	}

	public void setFromDateInDF(Date fromDateInDF) {
		this.fromDateInDF = fromDateInDF;
	}

	public Date getToDateInDF() {
		return toDateInDF;
	}

	public void setToDateInDF(Date toDateInDF) {
		this.toDateInDF = toDateInDF;
	}

	public String getDevContact() {
		return devContact;
	}

	public void setDevContact(String devContact) {
		if (StringUtils.isStringNull(devContact)) {
			this.devContact = null;
		} else {
			this.devContact = devContact;
		}
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		if (StringUtils.isStringNull(fromDate)) {
			this.fromDate = null;
			this.fromDateInDF = null;
		} else {
			this.fromDate = fromDate;
			try {
				this.fromDateInDF = df.parse(fromDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		if (StringUtils.isStringNull(toDate)) {
			this.toDate = null;
			this.toDateInDF = null;
		} else {
			this.toDate = toDate;
			try {
				this.toDateInDF = df.parse(toDate);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		if (StringUtils.isStringNull(envName)) {
			this.envName = null;
		} else {
			this.envName = envName;
		}
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		if (StringUtils.isStringNull(manager)) {
			this.manager = null;
		} else {
			this.manager = manager;
		}
	}

	public String getBookedBy() {
		return bookedBy;
	}

	public void setBookedBy(String bookedBy) {
		if (StringUtils.isStringNull(bookedBy)) {
			this.bookedBy = null;
		} else {
			this.bookedBy = bookedBy;
		}
	}

	public String getBaContact() {
		return baContact;
	}

	public void setBaContact(String baContact) {
		if (StringUtils.isStringNull(baContact)) {
			this.baContact = null;
		} else {
			this.baContact = baContact;
		}
	}

	public String getQaContact() {
		return qaContact;
	}

	public void setQaContact(String qaContact) {
		if (StringUtils.isStringNull(qaContact)) {
			this.qaContact = null;
		} else {
			this.qaContact = qaContact;
		}
	}

	public String getStakeholder() {
		return stakeholder;
	}

	public void setStakeholder(String stakeholder) {
		if (StringUtils.isStringNull(stakeholder)) {
			this.stakeholder = null;
		} else {
			this.stakeholder = stakeholder;
		}
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		if (StringUtils.isStringNull(projectName)) {
			this.projectName = null;
		} else {
			this.projectName = projectName;
		}
	}

	public String getReqSummary() {
		return reqSummary;
	}

	public void setReqSummary(String reqSummary) {
		if (StringUtils.isStringNull(reqSummary)) {
			this.reqSummary = null;
		} else {
			this.reqSummary = reqSummary;
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

	public String getNotifies() {
		return notifies;
	}

	public void setNotifies(String notifies) {
		if (StringUtils.isStringNull(notifies)) {
			this.notifies = null;
		} else {
			this.notifies = notifies.replaceAll(" ", "");
		}
	}	

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		if (StringUtils.isStringNull(business)) {
			this.business = null;
		} else {
			this.business = business;
		}
	}

	@Override
	public String toString() {
		return "BookConfigDTO [envName=" + envName + ", projectName="
				+ projectName + ", baContact=" + baContact + ", manager="
				+ manager + ", devContact=" + devContact + ", qaContact="
				+ qaContact + ", stakeholder=" + stakeholder + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", bookedBy=" + bookedBy
				+ ", bookingStatus=" + bookingStatus + ", emailAddress="
				+ emailAddress + ", lineNo=" + lineNo + ", region=" + region
				+ ", reason=" + reason + ", reqSummary=" + reqSummary
				+ ", notifies=" + notifies + ", business=" + business + ", df="
				+ df + ", fromDateInDF=" + fromDateInDF + ", toDateInDF="
				+ toDateInDF + ", error=" + error + ", message=" + message
				+ ", showExpiredBookings=" + showExpiredBookings + "]";
	}

}
