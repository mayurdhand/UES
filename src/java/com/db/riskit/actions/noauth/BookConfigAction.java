package com.db.riskit.actions.noauth;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.BookConfigDTO;
import com.db.riskit.dto.ComboDTO;
import com.db.riskit.utils.EmailApi;
import com.db.riskit.utils.TemplateUtil;
import com.db.riskit.utils.file.ConfigBookingFileOperations;
import com.db.riskit.utils.file.RequestFileOperations;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.mail.smtp.SMTPAddressFailedException;

import freemarker.template.Template;

public class BookConfigAction extends RiskItActionSupport implements ModelDriven<BookConfigDTO> {

	private static final long serialVersionUID = 1L;
	private BookConfigDTO bookConfigDTO;
	private List<BookConfigDTO> bookConfigDTOList;
	private TreeSet<ComboDTO> envSet;	
	private TreeSet<ComboDTO> freeEnvSet;
	private Map<String, ArrayList<BookConfigDTO>> bookingMap;
	private InputStream inputStream;
	private boolean success = false;	
	private static TreeSet<ComboDTO> dbSymphonyEnvSet = getDBSymphonyEnvFromCSV();
	private static TreeSet<ComboDTO> dbSymphonyAppSet;
	
	public TreeSet<ComboDTO> getDbSymphonyEnvSet() {
		return dbSymphonyEnvSet;
	}
	
	public TreeSet<ComboDTO> getDbSymphonyAppSet() {
		return dbSymphonyAppSet;
	}
	
	private static void setDbSymphonyAppSet(TreeSet<ComboDTO> appSet) {
		dbSymphonyAppSet = appSet;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Map<String, ArrayList<BookConfigDTO>> getBookingMap() {
		return bookingMap;
	}

	public void setBookingMap(Map<String, ArrayList<BookConfigDTO>> bookingMap) {
		this.bookingMap = bookingMap;
	}

	public TreeSet<ComboDTO> getFreeEnvSet() {
		return freeEnvSet;
	}

	public void setFreeEnvSet(TreeSet<ComboDTO> freeEnvSet) {
		this.freeEnvSet = freeEnvSet;
	}

	public List<BookConfigDTO> getBookConfigDTOList() {
		return bookConfigDTOList;
	}

	public void setBookConfigDTOList(List<BookConfigDTO> bookConfigDTOList) {
		this.bookConfigDTOList = bookConfigDTOList;
	}

	public TreeSet<ComboDTO> getEnvSet() {
		return envSet;
	}

	public void setEnvSet(TreeSet<ComboDTO> envSet) {
		this.envSet = envSet;
	}

	public BookConfigDTO getModel() {
		return this.bookConfigDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareBookConfigDTO();
	}

	private void prepareBookConfigDTO() {
		if (this.bookConfigDTO == null)
			this.bookConfigDTO = new BookConfigDTO();
	}

	public String execute() {		
		return "noauthbca";
	}
	
	private static TreeSet<ComboDTO>  getDBSymphonyEnvFromCSV() {
		String path = null;
		BufferedReader br = null;
		String[] data = null;
		TreeSet<ComboDTO>  appSet = new TreeSet<ComboDTO>();
		TreeSet<ComboDTO>  envSet = new TreeSet<ComboDTO>();
		try {
			path = PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_DBSYMPHONY_ENV;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				data = strLine.split(",");
				ComboDTO comboAppDTO =  new ComboDTO();
				ComboDTO comboEnvDTO =  new ComboDTO();
				comboAppDTO.setCode(data[0]);
				comboAppDTO.setName(data[0]);
				comboEnvDTO.setParent(data[0]);
				comboEnvDTO.setCode(data[1]);
				comboEnvDTO.setName(data[2]);
				comboEnvDTO.setNewCode(data[3]);
				envSet.add(comboEnvDTO);
				appSet.add(comboAppDTO);
			}
			setDbSymphonyAppSet(appSet);
		} catch (Exception ex) {
			Logger.log(BookConfigAction.class.getName(), Logger.ERROR, Logger.getStackTrace(ex));
			envSet = null;
			setDbSymphonyAppSet(null);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Logger.log(BookConfigAction.class.getName(), Logger.ERROR, Logger.getStackTrace(e));
			}
		}
		return envSet;
	}
	
	public String loadDBSymphonyEnvCombos() {
		setSuccess(true);
		return "noauthbca";
	}
	
	private TreeSet<ComboDTO> getEnvFromCSV() {
		String path = null;
		BufferedReader br = null;
		String[] data = null;
		TreeSet<ComboDTO> envSet = new TreeSet<ComboDTO>();
		try {
			path = PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_ENV;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				data = strLine.split(",");
				ComboDTO comboDTO =  new ComboDTO();
				comboDTO.setName(data[2] + ":" + data[1] + ":" + data[0]);
				envSet.add(comboDTO);
			}
		} catch (Exception ex) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(ex));
			setEnvSet(null);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			}
		}
		return envSet;
	}

	private void loadCombos() {
		setEnvSet(getEnvFromCSV());
	}

	public String loadCombosWithAll() {
		loadCombos();		
		ComboDTO comboDTO =  new ComboDTO();
		comboDTO.setName("ALL");
		getEnvSet().add(comboDTO);
		setSuccess(true);
		return "noauthbca";
	}

	public String bookConfig() {
		if (getModel().getToDateInDF().compareTo(getModel().getFromDateInDF()) <= 0) {
			getModel().setMessage("To Date should be greater than From Date");
			return "noauthbca";
		}
		if (!isValidEmail(getModel().getEmailAddress())) {
			getModel().setMessage("Please enter a valid email id.");
			return "noauthbca";
		}		
		if (getModel().getReqSummary().length() > 500) {
			getModel().setMessage("Requirement Summary should not be more than 500 characters.");
			return "noauthbca";
		}
		if(getModel().getNotifies() != null){
			String [] data = getModel().getNotifies().split(",");
			for (String email : data) {
				if (!isValidEmail(email.trim())) {
					getModel().setMessage("Please enter valid comma',' seperated email(s) in 'Emails to notify'.");
					return "noauthbca";
				}
			}			
		}
		boolean isSuccess = RequestFileOperations.getInstance().makeBookingRequest(getModel());
		if (isSuccess) {
			sendRequestReceivedMail();
		} else {
			getModel().setMessage("Unable to make request. Please try again.");
		}
		return "noauthbca";
	}

	private void sendRequestReceivedMail() {
		int status = sendMail(getModel().getEmailAddress());
		if (status == EMAIL_SUCCESS) {
			getModel().setMessage("Config Booking Request Recieved");
			setSuccess(true);
		} else if (status == EMAIL_FAILURE) {
			getModel().setMessage("Unable to make request. Please try again.");
			RequestFileOperations.getInstance().deleteRequest(getModel());
		} else if (status == INVALID_ADDRESS) {
			getModel().setMessage("Unable to make request as the email-id you provided is invalid.");
			RequestFileOperations.getInstance().deleteRequest(getModel());
		}
	}

	private void getBookedEnv() {
		try{
			List<BookConfigDTO> bcDtoList = null;
			bcDtoList = ConfigBookingFileOperations.getInstance().getBookedEnv(getModel());
			if (bcDtoList != null && bcDtoList.size() == 0) {
				setBookConfigDTOList(null);		
				getModel().setMessage("No records found.");
			} else {			
				Collections.sort(bcDtoList);
				setBookConfigDTOList(bcDtoList);
			}
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	private void getBookedEnvWithoutDates() {
		try{
			ArrayList<BookConfigDTO> bcDtoList = null;
			bcDtoList = ConfigBookingFileOperations.getInstance().getBookedEnvWithoutDates(getModel());
			if (bcDtoList != null && bcDtoList.size() == 0) {
				setBookConfigDTOList(null);			
				getModel().setMessage("No records found.");
			} else {			
				Collections.sort(bcDtoList);
				setBookConfigDTOList(bcDtoList);
			}
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public String searchConfigBooking() {		
		int flag = 0;
		if (getModel().getEnvName() == null) {
			getModel().setMessage("Please select Environment.");
			return "noauthbca";
		}
		if (getModel().getFromDate() == null && getModel().getToDate() == null) {
			// do nothing
		} else {
			if (getModel().getFromDate() == null) {
				getModel().setMessage("Please select From Date");
				return "noauthbca";
			}
			if (getModel().getToDate() == null) {
				getModel().setMessage("Please select To Date.");
				return "noauthbca";
			}
			flag = 1;
		}
		if (flag == 0) {
			getBookedEnvWithoutDates();
		} else {		
			getBookedEnv();
		}
		setSuccess(true);
		return "noauthbca";
	}
	
	private boolean isValidEmail(String hex) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();

	}

	private Integer sendMail(String requesterEmail) {
		EmailApi mail = null;
		String[] to = null;
		String[] cc = null;
		try {
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RE_BOOKING_REQUEST);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(REB_FIELD_PROJECT, getModel().getProjectName());
			rootMap.put(REB_FIELD_MANAGER, getModel().getManager());
			rootMap.put(REB_FIELD_BUSINESS, getModel().getBusiness());
			rootMap.put(REB_FIELD_FROM_DATE, getModel().getFromDate());
			rootMap.put(REB_FIELD_TO_DATE, getModel().getToDate());
			rootMap.put(REB_FIELD_BOOKED_BY, getModel().getBookedBy());
			rootMap.put(REB_FIELD_BA_CONTACT, getModel().getBaContact());
			rootMap.put(REB_FIELD_QA_CONTACT, getModel().getQaContact());
			rootMap.put(REB_FIELD_SENIOR_STAKEHOLDER, getModel().getStakeholder());
			rootMap.put(REB_FIELD_DEV_CONTACT, getModel().getDevContact());
			rootMap.put(REB_FIELD_EMAIL, getModel().getEmailAddress());
			rootMap.put(REB_FIELD_REGION, getModel().getRegion());
			rootMap.put(REB_FIELD_SUMMARY, getModel().getReqSummary());			
			Writer out = new StringWriter();
			template.process(rootMap, out);	
			
			String toString = null;
			if(getModel().getRegion().equalsIgnoreCase(REGION_SINGAPORE) || getModel().getRegion().equalsIgnoreCase(REGION_SYDNEY) ||  getModel().getRegion().equalsIgnoreCase(REGION_TOKYO)){
				toString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_APAC));
			} else if (getModel().getRegion().equalsIgnoreCase(REGION_NEW_YORK)) {
				toString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_NY));
			} else {
				toString = ((String) PropertyConfigurationFactory.getInstance().get("EMAIL_"+getModel().getBusiness()));
			}			
			to = toString.split(",");
			
			String ccString = null;
			if(getModel().getNotifies() != null){
				ccString = requesterEmail+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RE_EMAIL_CC))	+ "," + getModel().getNotifies();				
			} else {
				ccString = requesterEmail+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RE_EMAIL_CC));
			}			
			cc = ccString.split(",");
			
			Logger.log(this.getClass().getName(), Logger.INFO,"TO:"+Arrays.toString(to)+", CC:"+Arrays.toString(cc));

			mail = new EmailApi();
			mail.setFrom(FROM_ADDRESS);
			mail.setRecipientsTo(to);
			mail.setRecipientsCc(cc);
			mail.setSentDate(new java.util.Date());
			mail.setMimeType(EmailApi.HTML);
			mail.setSubject("CONFIG booking request received but not yet confirmed.");
			mail.setText(out.toString());
			
			if (mail.send("")) {
				Logger.log(this.getClass().getName(), Logger.INFO,"Mail sent successfully.");
			} else {
				Logger.log(this.getClass().getName(), Logger.INFO,"Failed to send mail.");
			}
			return EMAIL_SUCCESS;
		} catch (SMTPAddressFailedException e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			return INVALID_ADDRESS;
		} catch (Exception gEx) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(gEx));
			return EMAIL_FAILURE;
		}
	}

	private Date getCurrentDate() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();
		return date;
	}

	private boolean isAvailable(String env) {
		ArrayList<BookConfigDTO> bookConfigDTOList = null;
		try {
			if (getBookingMap() == null || getBookingMap().size() == 0) {
				return false;
			} else if (getBookingMap().get(env) == null	|| getBookingMap().get(env).size() == 0) {
				return true;
			} else {
				bookConfigDTOList = getBookingMap().get(env);
				for (BookConfigDTO tempDTO : bookConfigDTOList) {
					if (tempDTO.getToDateInDF().compareTo(getCurrentDate()) > 0)
						return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void prepareBookingsMap() {
		setBookingMap(ConfigBookingFileOperations.getInstance().prepareBookingsMap());
	}

	public void checkFreeEnv() {
		TreeSet<ComboDTO> freeEnvSet = null;
		try {
			prepareBookingsMap(); /***** Prepare Bookings Map before checking env avaliability ****/
			freeEnvSet = new TreeSet<ComboDTO>();
			TreeSet<ComboDTO> envSet = getEnvFromCSV();
			for (ComboDTO comboDTO : envSet) {
				if (isAvailable(comboDTO.getName())) {
					freeEnvSet.add(comboDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (freeEnvSet.size() == 0) {
			setFreeEnvSet(null);
		} else {
			setFreeEnvSet(freeEnvSet);
		}
	}

	public String showFreeConfigs() {
		checkFreeEnv();
		return "noauthbca";
	}

	public String downloadFile() throws Exception {
		ArrayList<BookConfigDTO> bcDtoList = null;
		if (getModel().getFromDate() == null || getModel().getToDate() == null) {
			bcDtoList = ConfigBookingFileOperations.getInstance().getBookedEnvWithoutDates(getModel());
		} else {
			bcDtoList = ConfigBookingFileOperations.getInstance().getBookedEnv(getModel());
		}

		StringBuffer sb = new StringBuffer();
		sb.append("CONFIG NAME,");
		sb.append("PROJECT NAME,");
		sb.append("PROJECT MANAGER,");
		sb.append("BUSINESS,");
		sb.append("BOOKED BY,");
		sb.append("BA CONTACT,");
		sb.append("QA CONTACT,");
		sb.append("DEV CONTACT,");
		sb.append("SENIOR STAKEHOLDER,");
		sb.append("FROM DATE,");
		sb.append("TO DATE");
		sb.append("\n");
		for (BookConfigDTO temp : bcDtoList) {
			sb.append("\""+temp.getEnvName() + "\",");
			sb.append("\""+temp.getProjectName() + "\",");
			sb.append("\""+temp.getManager() + "\",");
			sb.append("\""+temp.getBusiness() + "\",");
			sb.append("\""+temp.getBookedBy() + "\",");
			sb.append("\""+temp.getBaContact() + "\",");
			sb.append("\""+temp.getQaContact() + "\",");
			sb.append("\""+temp.getDevContact() + "\",");
			sb.append("\""+temp.getStakeholder() + "\",");
			sb.append("\""+temp.getFromDate() + "\",");
			sb.append("\""+temp.getToDate() + "\",");
			sb.append("\n");
		}
		setInputStream(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
		return "downloadFile";
	}	

}
