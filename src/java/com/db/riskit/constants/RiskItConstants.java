package com.db.riskit.constants;


public interface RiskItConstants {
	
	public static final int REQUEST_SUCCESS = 1;
	public static final int REQUEST_FAILURE = 0;
	public static final int REQUEST_ALREADY_PROCESSED = 2;
	public static final int REQUEST_DUPLICATE_ENTRY = 3;
	public static final int RS_ALREADY_BOOKED = 4;
	
	public static final int INVALID_ADDRESS = 2;
	public static final int EMAIL_FAILURE = 0;
	public static final int EMAIL_SUCCESS = 1;
	
	public static final String FROM_ADDRESS = "risk-it_uat_support@list.db.com";
	
	public static final String RSB_FIELD_REJECT_REASON = "reason";
	public static final String RSB_FIELD_UAT_RS = "uatRS";
	public static final String RSB_FIELD_PROD_RS = "prodRS";
	public static final String RSB_FIELD_MANAGER = "manager"; 
	public static final String RSB_FIELD_BUSINESS = "business";
	public static final String RSB_FIELD_GO_LIVE_DATE = "goLiveDate";
	public static final String RSB_FIELD_DECOM_DATE = "decomDate";
	public static final String RSB_FIELD_BOOKED_BY = "bookedBy";
	public static final String RSB_FIELD_EMAIL = "email";
	public static final String RSB_FIELD_PROJECT = "project"; 
	public static final String RSB_FIELD_REGION = "region";
	public static final String RSB_FIELD_SUMMARY = "reqSummary";
	
	public static final String REB_FIELD_REJECT_REASON = "reason";
	public static final String REB_FIELD_ENV = "envName";
	public static final String REB_FIELD_PROJECT = "projectName";
	public static final String REB_FIELD_MANAGER = "manager";
	public static final String REB_FIELD_BUSINESS = "business";
	public static final String REB_FIELD_FROM_DATE = "from";
	public static final String REB_FIELD_TO_DATE = "to";
	public static final String REB_FIELD_BOOKED_BY = "bookedBy";
	public static final String REB_FIELD_BA_CONTACT = "baContact";
	public static final String REB_FIELD_QA_CONTACT = "qaContact";
	public static final String REB_FIELD_SENIOR_STAKEHOLDER = "ss";
	public static final String REB_FIELD_DEV_CONTACT = "devContact";
	public static final String REB_FIELD_EMAIL = "email";
	public static final String REB_FIELD_REGION = "region";
	public static final String REB_FIELD_SUMMARY = "reqSummary";	
	
	public static final String TEMPLATE_PATH = "/templates/";
	public static final String FILE_EMAIL_TEMPLATE_RE_APPROVED = "REApprovedMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RE_REJECT = "RERejectMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RE_UPDATE_BOOKING = "REUpdateBookingMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RE_UPDATE_REQUEST = "REUpdateRequestMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RE_BOOKING_REQUEST = "RERequestMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RE_EXPIRY = "REBookingExpiryMail.ftl";	
	
	public static final String FILE_EMAIL_TEMPLATE_RS_APPROVED = "RSApprovedMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RS_REJECT = "RSRejectMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RS_UPDATE_BOOKING = "RSUpdateBookingMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RS_UPDATE_REQUEST = "RSUpdateRequestMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RS_BOOKING_REQUEST = "RSRequestMail.ftl";
	public static final String FILE_EMAIL_TEMPLATE_RS_EXPIRY = "RSBookingExpiryMail.ftl";
	
	public static final String STATUS_PENDING = "PENDING";	
	public static final String STATUS_APPROVED = "APPROVED";	
	public static final String STATUS_REJECTED = "REJECTED";	
	public static final String STATUS_DECOMMISSIONED = "DECOMMISSIONED";
		
	public static final String FILE_ENV = "EnvironmentDetails.csv";	
	public static final String FILE_DBSYMPHONY_ENV = "DBSymphonyEnv.csv";
	public static final String FILE_CONTACTS_LINKS = "Contacts.xls";
	public static final String FILE_CC = "ConfigCache.xls";
	public static final String FILE_RE_BOOKING = "Schedules.xls";
	public static final String FILE_RE_REQUEST = "Requests.xls";
	public static final String FILE_RS_BOOKING = "RSSchedules.xls";
	public static final String FILE_RS_REQUEST = "RSRequests.xls";
	public static final String FILE_INVENTORY = "InventorySheet.xls";
	
	public static final String ENV_UAT = "UAT";
	public static final String ENV_QA = "QA";
	public static final String ENV_PROD_UAT = "PROD-UAT";
	
	public static final String REGION_SYDNEY = "SYD";
	public static final String REGION_SINGAPORE = "SIN";
	public static final String REGION_TOKYO = "TOK";
	public static final String REGION_NEW_YORK = "NY";
	public static final String REGION_LONDON = "LDN";
	
	public static final String PROPERTY_DATA_PATH = "DATA_PATH";
	public static final String PROPERTY_BACKUP_PATH = "BACKUP_PATH";
	public static final String PROPERTY_RE_EMAIL_CC = "EMAIL_CC";
	public static final String PROPERTY_RS_EMAIL_CC = "EMAIL_CC_RS";
	public static final String PROPERTY_EMAIL_APAC = "EMAIL_APAC";
	public static final String PROPERTY_EMAIL_NY = "EMAIL_NY";
	
}
