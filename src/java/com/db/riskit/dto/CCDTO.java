package com.db.riskit.dto;

import com.tree.utils.common.StringUtils;

public class CCDTO {
	private String config;
	private int configId;
	private String database;
	private String ccHost;
	private String ccPort;
	private String message;
	
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		if (StringUtils.isStringNull(config)) {
			this.config = null;
		} else {
			this.config = config;
		}
	}
	
	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		if (StringUtils.isStringNull(database)) {
			this.database = null;
		} else {
			this.database = database;
		}
	}

	public String getCcHost() {
		return ccHost;
	}

	public void setCcHost(String ccHost) {
		if (StringUtils.isStringNull(ccHost)) {
			this.ccHost = null;
		} else {
			this.ccHost = ccHost;
		}
	}

	public String getCcPort() {
		return ccPort;
	}

	public void setCcPort(String ccPort) {
		if (StringUtils.isStringNull(ccPort)) {
			this.ccPort = null;
		} else {
			this.ccPort = ccPort;
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
		return "CCDTO [config=" + config + ", configId=" + configId
				+ ", database=" + database + ", ccHost=" + ccHost + ", ccPort="
				+ ccPort + ", message=" + message + "]";
	}
	
}
