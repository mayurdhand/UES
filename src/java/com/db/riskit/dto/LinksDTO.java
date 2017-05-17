package com.db.riskit.dto;

import com.tree.utils.common.StringUtils;

public class LinksDTO {

	private int linkId;
	private String applicationName;
	private String url;
	private String applicationDesc;
	private String message;	
	
	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		if (StringUtils.isStringNull(applicationName)) {
			this.applicationName = null;
		} else {
			this.applicationName = applicationName;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (StringUtils.isStringNull(url)) {
			this.url = null;
		} else {
			this.url = url;
		}
	}

	public String getApplicationDesc() {
		return applicationDesc;
	}

	public void setApplicationDesc(String applicationDesc) {
		if (StringUtils.isStringNull(applicationDesc)) {
			this.applicationDesc = null;
		} else {
			this.applicationDesc = applicationDesc;
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
		return "LinksDTO [applicationName=" + applicationName + ", url=" + url
				+ ", applicationDesc=" + applicationDesc + ", message="
				+ message + "]";
	}

}
