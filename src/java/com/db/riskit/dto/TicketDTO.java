package com.db.riskit.dto;

import com.tree.utils.common.StringUtils;

public class TicketDTO {
	private String longDesc;
	private String shortDesc;
	private String rptBy;
	private String affectedCi;
	private String inCode;
	private String error;	
	
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		if (StringUtils.isStringNull(longDesc)) {
			this.longDesc = null;
		} else {
			this.longDesc = longDesc;
		}
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		if (StringUtils.isStringNull(shortDesc)) {
			this.shortDesc = null;
		} else {
			this.shortDesc = shortDesc;
		}
	}
	public String getRptBy() {
		return rptBy;
	}
	public void setRptBy(String rptBy) {
		if (StringUtils.isStringNull(rptBy)) {
			this.rptBy = null;
		} else {
			this.rptBy = rptBy;
		}
	}
	public String getAffectedCi() {
		return affectedCi;
	}
	public void setAffectedCi(String affectedCi) {
		if (StringUtils.isStringNull(affectedCi)) {
			this.affectedCi = null;
		} else {
			this.affectedCi = affectedCi;
		}
	}
	public String getInCode() {
		return inCode;
	}
	public void setInCode(String inCode) {
		if (StringUtils.isStringNull(inCode)) {
			this.inCode = null;
		} else {
			this.inCode = inCode;
		}
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		if (StringUtils.isStringNull(error)) {
			this.error = null;
		} else {
			this.error = error;
		}
	}
	
	@Override
	public String toString() {
		return "TicketDTO [longDesc=" + longDesc + ", shortDesc=" + shortDesc
				+ ", rptBy=" + rptBy + ", affectedCi=" + affectedCi
				+ ", inCode=" + inCode + ", error=" + error + "]";
	}
	
}
