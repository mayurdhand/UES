package com.db.riskit.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tree.utils.common.StringUtils;

public class BookRSDTO{
	private String uatRS;
	private String prodRS;
	private String projectName;
	private String manager;
	private String goLiveDate;
	private String decomDate;
	private String bookedBy;
	private String bookingStatus;
	private String emailAddress;

	private Integer lineNo;
	private String region;
	private String reason;
	private String reqSummary;
	private String notifies;
	private String decomIds;
	private String business;

	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	private Date goLiveDateInDF;
	private Date decomDateInDF;
	private boolean error = false;
	private String message;

	public String getUatRS() {
		return uatRS;
	}

	public void setUatRS(String uatRS) {
		if (StringUtils.isStringNull(uatRS)) {
			this.uatRS = null;
		} else {
			this.uatRS = uatRS;
		}
	}

	public String getProdRS() {
		return prodRS;
	}

	public void setProdRS(String prodRS) {
		if (StringUtils.isStringNull(prodRS)) {
			this.prodRS = null;
		} else {
			this.prodRS = prodRS;
		}
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

	public Date getGoLiveDateInDF() {
		return goLiveDateInDF;
	}

	public void setGoLiveDateInDF(Date goLiveDateInDF) {
		this.goLiveDateInDF = goLiveDateInDF;
	}

	public Date getDecomDateInDF() {
		return decomDateInDF;
	}

	public void setDecomDateInDF(Date decomDateInDF) {
		this.decomDateInDF = decomDateInDF;
	}

	public String getGoLiveDate() {
		return goLiveDate;
	}

	public void setGoLiveDate(String goLiveDate) {
		if (StringUtils.isStringNull(goLiveDate)) {
			this.goLiveDate = null;
			this.goLiveDateInDF = null;
		} else {
			this.goLiveDate = goLiveDate;
			try {
				this.goLiveDateInDF = df.parse(goLiveDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public String getDecomDate() {
		return decomDate;
	}

	public void setDecomDate(String decomDate) {
		if (StringUtils.isStringNull(decomDate)) {
			this.decomDate = null;
			this.decomDateInDF = null;
		} else {
			this.decomDate = decomDate;
			try {
				this.decomDateInDF = df.parse(decomDate);

			} catch (ParseException e) {
				e.printStackTrace();
			}
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

	public String getDecomIds() {
		return decomIds;
	}

	public void setDecomIds(String decomIds) {
		if (StringUtils.isStringNull(decomIds)) {
			this.decomIds = null;
		} else {
			this.decomIds = decomIds;
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
		return "BookRSDTO [uatRS=" + uatRS + ", prodRS=" + prodRS
				+ ", projectName=" + projectName + ", manager=" + manager
				+ ", goLiveDate=" + goLiveDate + ", decomDate=" + decomDate
				+ ", bookedBy=" + bookedBy + ", bookingStatus=" + bookingStatus
				+ ", emailAddress=" + emailAddress + ", lineNo=" + lineNo
				+ ", region=" + region + ", reason=" + reason + ", reqSummary="
				+ reqSummary + ", notifies=" + notifies + ", decomIds="
				+ decomIds + ", business=" + business + ", df=" + df
				+ ", goLiveDateInDF=" + goLiveDateInDF + ", decomDateInDF="
				+ decomDateInDF + ", error=" + error + ", message=" + message
				+ "]";
	}

}
