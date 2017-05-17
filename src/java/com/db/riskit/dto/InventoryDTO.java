package com.db.riskit.dto;

import com.tree.utils.common.StringUtils;

public class InventoryDTO {

	private Integer sno;
	private String configName;
	private String region;
	private String jsm;
	private String cacheTrades;
	private String env;
	private String physicalHost;
	private String statusServerHost;
	private String dbName;
	private String dbHost;
	private String dbPort;
	private String reHardBounceTime;
	private String reSoftBounceTime;
	private String reHardBounceDays;
	private String reSoftBounceDays;
	private String ssBounceTime;
	private String ssBounceDays;
	private String reVersion;
	private String defaultDBAX;
	private String dbaxParserVersion;
	private String dwebNode;
	private String heading;
	private String ccInstance;
	private String ccVersion;
	private String jsmInstance;
	private String jsmVersion;
	private String tdcInstance;
	private String tdcVersion;
	private String jsmSchema;
	private String jsmSchemaVersion;

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		if (StringUtils.isStringNull(heading)) {
			this.heading = null;
		} else {
			this.heading = heading;
		}
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		if (StringUtils.isStringNull(configName)) {
			this.configName = null;
		} else {
			this.configName = configName;
		}
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		if (StringUtils.isStringNull(env)) {
			this.env = null;
		} else {
			this.env = env;
		}
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

	public String getPhysicalHost() {
		return physicalHost;
	}

	public void setPhysicalHost(String physicalHost) {
		if (StringUtils.isStringNull(physicalHost)) {
			this.physicalHost = null;
		} else {
			this.physicalHost = physicalHost;
		}
	}

	public String getStatusServerHost() {
		return statusServerHost;
	}

	public void setStatusServerHost(String statusServerHost) {
		if (StringUtils.isStringNull(statusServerHost)) {
			this.statusServerHost = null;
		} else {
			this.statusServerHost = statusServerHost;
		}
	}

	public String getReVersion() {
		return reVersion;
	}

	public void setReVersion(String reVersion) {
		if (StringUtils.isStringNull(reVersion)) {
			this.reVersion = null;
		} else {
			this.reVersion = reVersion;
		}
	}

	public String getDefaultDBAX() {
		return defaultDBAX;
	}

	public void setDefaultDBAX(String defaultDBAX) {
		if (StringUtils.isStringNull(defaultDBAX)) {
			this.defaultDBAX = null;
		} else {
			this.defaultDBAX = defaultDBAX;
		}
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		if (StringUtils.isStringNull(dbName)) {
			this.dbName = null;
		} else {
			this.dbName = dbName;
		}
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		if (StringUtils.isStringNull(dbPort)) {
			this.dbPort = null;
		} else {
			this.dbPort = dbPort;
		}
	}

	public String getSsBounceTime() {
		return ssBounceTime;
	}

	public void setSsBounceTime(String ssBounceTime) {
		if (StringUtils.isStringNull(ssBounceTime)) {
			this.ssBounceTime = null;
		} else {
			this.ssBounceTime = ssBounceTime;
		}
	}

	public String getSsBounceDays() {
		return ssBounceDays;
	}

	public void setSsBounceDays(String ssBounceDays) {
		if (StringUtils.isStringNull(ssBounceDays)) {
			this.ssBounceDays = null;
		} else {
			this.ssBounceDays = ssBounceDays;
		}
	}

	public String getDbaxParserVersion() {
		return dbaxParserVersion;
	}

	public void setDbaxParserVersion(String dbaxParserVersion) {
		if (StringUtils.isStringNull(dbaxParserVersion)) {
			this.dbaxParserVersion = null;
		} else {
			this.dbaxParserVersion = dbaxParserVersion;
		}
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		if (StringUtils.isStringNull(dbHost)) {
			this.dbHost = null;
		} else {
			this.dbHost = dbHost;
		}
	}	

	public String getReHardBounceTime() {
		return reHardBounceTime;
	}

	public void setReHardBounceTime(String reHardBounceTime) {
		if (StringUtils.isStringNull(reHardBounceTime)) {
			this.reHardBounceTime = null;
		} else {
			this.reHardBounceTime = reHardBounceTime;
		}
	}

	public String getReSoftBounceTime() {
		return reSoftBounceTime;
	}

	public void setReSoftBounceTime(String reSoftBounceTime) {
		if (StringUtils.isStringNull(reSoftBounceTime)) {
			this.reSoftBounceTime = null;
		} else {
			this.reSoftBounceTime = reSoftBounceTime;
		}
	}

	public String getReHardBounceDays() {
		return reHardBounceDays;
	}

	public void setReHardBounceDays(String reHardBounceDays) {
		if (StringUtils.isStringNull(reHardBounceDays)) {
			this.reHardBounceDays = null;
		} else {
			this.reHardBounceDays = reHardBounceDays;
		}
	}

	public String getReSoftBounceDays() {
		return reSoftBounceDays;
	}

	public void setReSoftBounceDays(String reSoftBounceDays) {
		if (StringUtils.isStringNull(reSoftBounceDays)) {
			this.reSoftBounceDays = null;
		} else {
			this.reSoftBounceDays = reSoftBounceDays;
		}
	}
	

	public String getDwebNode() {
		return dwebNode;
	}

	public void setDwebNode(String dwebNode) {
		if (StringUtils.isStringNull(dwebNode)) {
			this.dwebNode = null;
		} else {
			this.dwebNode = dwebNode;
		}
	}	

	public String getJsm() {
		return jsm;
	}

	public void setJsm(String jsm) {
		if (StringUtils.isStringNull(jsm)) {
			this.jsm = null;
		} else {
			this.jsm = jsm;
		}
	}

	public String getCacheTrades() {
		return cacheTrades;
	}

	public void setCacheTrades(String cacheTrades) {
		if (StringUtils.isStringNull(cacheTrades)) {
			this.cacheTrades = null;
		} else {
			this.cacheTrades = cacheTrades;
		}
	}
	
	

	public String getCcInstance() {
		return ccInstance;
	}

	public void setCcInstance(String ccInstance) {
		if (StringUtils.isStringNull(ccInstance)) {
			this.ccInstance = null;
		} else {
			this.ccInstance = ccInstance;
		}
	}

	public String getCcVersion() {
		return ccVersion;
	}

	public void setCcVersion(String ccVersion) {
		if (StringUtils.isStringNull(ccVersion)) {
			this.ccVersion = null;
		} else {
			this.ccVersion = ccVersion;
		}
	}

	public String getJsmInstance() {
		return jsmInstance;
	}

	public void setJsmInstance(String jsmInstance) {
		if (StringUtils.isStringNull(jsmInstance)) {
			this.jsmInstance = null;
		} else {
			this.jsmInstance = jsmInstance;
		}
	}

	public String getJsmVersion() {
		return jsmVersion;
	}

	public void setJsmVersion(String jsmVersion) {
		if (StringUtils.isStringNull(jsmVersion)) {
			this.jsmVersion = null;
		} else {
			this.jsmVersion = jsmVersion;
		}
	}	

	public String getTdcInstance() {
		return tdcInstance;
	}

	public void setTdcInstance(String tdcInstance) {
		if (StringUtils.isStringNull(tdcInstance)) {
			this.tdcInstance = null;
		} else {
			this.tdcInstance = tdcInstance;
		}
	}

	public String getTdcVersion() {
		return tdcVersion;
	}

	public void setTdcVersion(String tdcVersion) {
		if (StringUtils.isStringNull(tdcVersion)) {
			this.tdcVersion = null;
		} else {
			this.tdcVersion = tdcVersion;
		}
	}	

	public String getJsmSchema() {
		return jsmSchema;
	}

	public void setJsmSchema(String jsmSchema) {
		if (StringUtils.isStringNull(jsmSchema)) {
			this.jsmSchema = null;
		} else {
			this.jsmSchema = jsmSchema;
		}
	}

	public String getJsmSchemaVersion() {
		return jsmSchemaVersion;
	}

	public void setJsmSchemaVersion(String jsmSchemaVersion) {
		if (StringUtils.isStringNull(jsmSchemaVersion)) {
			this.jsmSchemaVersion = null;
		} else {
			this.jsmSchemaVersion = jsmSchemaVersion;
		}
	}

	@Override
	public String toString() {
		return "InventoryDTO [sno=" + sno + ", configName=" + configName
				+ ", region=" + region + ", jsm=" + jsm + ", cacheTrades="
				+ cacheTrades + ", env=" + env + ", physicalHost="
				+ physicalHost + ", statusServerHost=" + statusServerHost
				+ ", dbName=" + dbName + ", dbHost=" + dbHost + ", dbPort="
				+ dbPort + ", reHardBounceTime=" + reHardBounceTime
				+ ", reSoftBounceTime=" + reSoftBounceTime
				+ ", reHardBounceDays=" + reHardBounceDays
				+ ", reSoftBounceDays=" + reSoftBounceDays + ", ssBounceTime="
				+ ssBounceTime + ", ssBounceDays=" + ssBounceDays
				+ ", reVersion=" + reVersion + ", defaultDBAX=" + defaultDBAX
				+ ", dbaxParserVersion=" + dbaxParserVersion + ", dwebNode="
				+ dwebNode + ", heading=" + heading + ", ccInstance="
				+ ccInstance + ", ccVersion=" + ccVersion + ", jsmInstance="
				+ jsmInstance + ", jsmVersion=" + jsmVersion + ", tdcInstance="
				+ tdcInstance + ", tdcVersion=" + tdcVersion + ", jsmSchema="
				+ jsmSchema + ", jsmSchemaVersion=" + jsmSchemaVersion + "]";
	}

}
