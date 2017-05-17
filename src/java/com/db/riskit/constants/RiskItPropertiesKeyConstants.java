package com.db.riskit.constants;

public interface RiskItPropertiesKeyConstants {	
	String SAP_CONFIGURAION_FILE_NAME = "riskit_sap.properties";
	String LOAD_PROPERTIES_FILE_FROM = "source";
	String LOG_PATH = "d:\\RISK-IT\\UESLogs\\UES.log";
	String SAP_CONFIGURAION_FILE_NAME_WITH_PATH = "d:\\RISK-IT\\properties\\riskit_sap.properties";
	/*
	 * LOAD_PROPERTIES_FILE_FROM can be set to source to load it from classpath
	 * LOAD_PROPERTIES_FILE_FROM can be set anything other than "source" to load
	 * it from SAP_CONFIGURAION_FILE_NAME_WITH_PATH
	 */
}
