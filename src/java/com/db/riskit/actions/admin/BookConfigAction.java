package com.db.riskit.actions.admin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.BookConfigDTO;
import com.db.riskit.dto.ComboDTO;
import com.db.riskit.enums.ReviewMailType;
import com.db.riskit.scheduler.re.ConfigBookingExpieryMailScheduler;
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
	private TreeSet<ComboDTO> freeEnvSet;
	private Map<String, ArrayList<BookConfigDTO>> bookingMap;
	private String mailtext;
	private String mailSubject;
	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMailtext() {
		return mailtext;
	}

	public void setMailtext(String mailtext) {
		this.mailtext = mailtext;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
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
	
	public BookConfigDTO getBookConfigDTO() {
		return bookConfigDTO;
	}

	public void setBookConfigDTO(BookConfigDTO bookConfigDTO) {
		this.bookConfigDTO = bookConfigDTO;
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
	
	public Map<String, ArrayList<BookConfigDTO>> getBookingMap() {
		return bookingMap;
	}

	public void setBookingMap(Map<String, ArrayList<BookConfigDTO>> bookingMap) {
		this.bookingMap = bookingMap;
	}

	public String showPendingRequests() {
		List<BookConfigDTO> bcDtoList = null;
		bcDtoList = RequestFileOperations.getInstance().getPendingRequestsList();
		if (bcDtoList != null && bcDtoList.size() == 0) {
			setBookConfigDTOList(null);
			getModel().setMessage("No pending requests.");
		} else {
			setBookConfigDTOList(bcDtoList);
		}
		setSuccess(true);
		return "showPendingRequests";
	}

	public String execute() {
		setSuccess(true);
		return "showPendingRequests";
	}

	public String start() {
		if (!ConfigBookingExpieryMailScheduler.getInstance().isRunning()) {
			ConfigBookingExpieryMailScheduler.getInstance().start();
			getModel().setMessage("RE Checker started successfully.");
		} else {
			getModel().setMessage("RE Checker is already running.");
		}
		setSuccess(true);
		return "scheduler";
	}

	public String stop() {
		if (ConfigBookingExpieryMailScheduler.getInstance().isRunning()) {
			ConfigBookingExpieryMailScheduler.getInstance().stop();
			getModel().setMessage("RE Checker stopped successfully.");
		} else {
			getModel().setMessage("RE Checker is already stopped.");
		}
		setSuccess(true);
		return "scheduler";
	}

	public String status() {
		if (ConfigBookingExpieryMailScheduler.getInstance().isRunning()) {
			getModel().setMessage("RE Checker is running.");
		} else {
			getModel().setMessage("RE Checker is stopped.");
		}
		setSuccess(true);
		return "scheduler";
	}
	
	private void prepareBookingsMap() {
		setBookingMap(ConfigBookingFileOperations.getInstance().prepareBookingsMap());
	}

	private boolean isAvailable(String env, boolean isEditCheck) {
		ArrayList<BookConfigDTO> bookConfigDTOList = null;
		try {
			if (getBookingMap() == null || getBookingMap().size() == 0) {
				return false;
			} else if (getBookingMap().get(env) == null	|| getBookingMap().get(env).size() == 0) {
				return true;
			} else {
				bookConfigDTOList = getBookingMap().get(env);				
				for (BookConfigDTO tempDTO : bookConfigDTOList) {
					if ( !isEditCheck 
							|| (isEditCheck && 
									(getModel().getLineNo().intValue() != tempDTO.getLineNo().intValue()) && (env.split(":")[1].equals(ENV_QA))
								) 
						) 
					{
							if ((getModel().getFromDateInDF().compareTo(tempDTO.getFromDateInDF()) >= 0 
								 && 
								 getModel().getFromDateInDF().compareTo(tempDTO.getToDateInDF()) <= 0
									) || (
										getModel().getToDateInDF().compareTo(tempDTO.getFromDateInDF()) >= 0 
										&& 
										getModel().getToDateInDF().compareTo(tempDTO.getToDateInDF()) <= 0
									) || (
										getModel().getFromDateInDF().compareTo(tempDTO.getFromDateInDF()) <= 0 
										&& 
										getModel().getToDateInDF().compareTo(tempDTO.getToDateInDF()) >= 0
									)
								) 
						{
							return false;
						}
					}
				}
			}

		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		return true;
	}

	private Map<String, TreeSet<String>> getEnvMap() {
		String path = null;
		BufferedReader br = null;
		String[] data = null;
		Map<String, TreeSet<String>> envMap = null;
		try {
			path = PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_ENV;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String strLine;
			envMap = new HashMap<String, TreeSet<String>>();
			while ((strLine = br.readLine()) != null) {
				data = strLine.split(",");
				if (envMap.get(data[2]) == null) {
					envMap.put(data[2], new TreeSet<String>());
				}
				envMap.get(data[2]).add(data[2] + ":" + data[1] + ":" + data[0]);
			}
		} catch (Exception ex) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(ex));
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			}
		}
		return envMap;
	}

	private void checkFreeEnv() {
		TreeSet<ComboDTO> freeEnvSet = null;
		TreeSet<String> envSet = null;
		String[] tempEnv = null;
		try {			
			prepareBookingsMap(); /***** Prepare Bookings Map before checking env avaliability ****/
			freeEnvSet = new TreeSet<ComboDTO>();
			envSet = getEnvMap().get(getModel().getRegion());
			for (String env : envSet) {
				tempEnv = env.split(":");
				if (tempEnv[1].equalsIgnoreCase(ENV_UAT) || tempEnv[1].equalsIgnoreCase(ENV_PROD_UAT)) {
					ComboDTO comboDTO =  new ComboDTO();
					comboDTO.setName(tempEnv[0] + ":" + tempEnv[1] + ":" + tempEnv[2]);
					freeEnvSet.add(comboDTO);
				} else if (isAvailable(env, false)) {
					ComboDTO comboDTO =  new ComboDTO();
					comboDTO.setName(tempEnv[0] + ":" + tempEnv[1] + ":" + tempEnv[2]);
					freeEnvSet.add(comboDTO);
				}
			}
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		if (freeEnvSet.size() == 0) {
			setFreeEnvSet(null);
		} else {
			setFreeEnvSet(freeEnvSet);
		}
	}

	public String allotConfig() {
		checkFreeEnv();
		setSuccess(true);
		return "allotConfig";
	}

	public String addToSchedules() {
		int requestStatus = RequestFileOperations.getInstance().updateStatus(getModel(), STATUS_APPROVED);
		switch (requestStatus) {
		case REQUEST_FAILURE:
			getModel().setMessage("Unable to process request. Please try again.");
			break;
		case REQUEST_SUCCESS:
			boolean isSuccess = ConfigBookingFileOperations.getInstance().addToSchedules(getModel());
			if (isSuccess) {
				sendApprovedMail();
				setSuccess(true);
			} else {
				getModel().setMessage("Unable to process request. Please try again.");
				RequestFileOperations.getInstance().updateStatus(getModel(),STATUS_PENDING);
			}
			break;
		case REQUEST_ALREADY_PROCESSED:
			getModel().setMessage("Request already processed.");
		}		
		return "showPendingRequests";
	}

	private void sendApprovedMail() {
		int emailStatus = sendMail(getModel().getEmailAddress(),ReviewMailType.APPROVED);
		if (emailStatus == EMAIL_SUCCESS) {
			getModel().setMessage("Config alloted successfully.");
		} else {
			getModel().setMessage("Config alloted successfully but unable to send email.");
		}
	}

	public String changeRequestToRejected() {
		int	requestStatus = RequestFileOperations.getInstance().updateStatus(getModel(), STATUS_REJECTED);		
		switch (requestStatus) {
		case REQUEST_FAILURE:
			getModel().setMessage("Unable to process request. Please try again.");
			break;
		case REQUEST_SUCCESS:
			setSuccess(true);
			sendRejectMail();			
			break;
		case REQUEST_ALREADY_PROCESSED:
			getModel().setMessage("Request already processed.");
			break;
		}		
		return "showPendingRequests";
	}

	private void sendRejectMail() {
		int emailStatus = sendMail(getModel().getEmailAddress(),ReviewMailType.REJECTED);
		if (emailStatus == RiskItConstants.REQUEST_SUCCESS) {
			getModel().setMessage("Request rejected successfully.");
		} else {
			getModel().setMessage("Request rejected successfully but unable to send email.");
		}
	}

	public String updateRequest() {		
		if (getModel().getToDateInDF().compareTo(getModel().getFromDateInDF()) < 0) {
			getModel().setMessage("To Date should be greater than or equal to From Date");
			return "showPendingRequests";
		}
		if(getModel().getNotifies() != null){
			String [] data = getModel().getNotifies().split(",");
			for (String email : data) {
				if (!isValidEmail(email.trim())) {
					getModel().setMessage("Please enter valid comma',' seperated email(s) in 'Emails to notify'.");
					return "showPendingRequests";
				}
			}			
		}
		int status = RequestFileOperations.getInstance().updateRequest(getModel());
		switch (status) {
		case REQUEST_FAILURE:
			getModel().setMessage("Unable to process request. Please try again.");
			break;
		case REQUEST_SUCCESS:
			setSuccess(true);
			sendUpdateRequestMail();			
			break;
		case REQUEST_ALREADY_PROCESSED:
			getModel().setMessage("Request already processed.");
			break;
		}		
		return "showPendingRequests";
	}

	private void sendUpdateRequestMail() {
		int emailStatus = sendMail(getModel().getEmailAddress(),ReviewMailType.UPDATE_REQUEST);
		if (emailStatus == RiskItConstants.REQUEST_SUCCESS) {
			getModel().setMessage("Request updated successfully.");
		} else {
			getModel().setMessage("Request updated successfully but unable to send email.");
		}
	}

	public String editBooking() {
		return "editBooking";
	}

	public String updateBooking() {
		if (!isValidEmail(getModel().getEmailAddress())) {
			getModel().setMessage("Please enter a valid email id.");
			return "editBooking";
		}
		if(getModel().getNotifies() != null){
			String [] data = getModel().getNotifies().split(",");
			for (String email : data) {
				if (!isValidEmail(email.trim())) {
					getModel().setMessage("Please enter valid comma',' seperated email(s) in 'Emails to notify'.");
					return "showPendingRequests";
				}
			}			
		}
		if (getModel().getToDateInDF().compareTo(getModel().getFromDateInDF()) <= 0) {
			getModel().setMessage("To Date should be greater than or equal to From Date");
			return "editBooking";
		}
		if (getModel().getReqSummary().length() > 500) {
			getModel().setMessage("Requirement Summary should not be more than 500 characters.");
			return "editBooking";
		}
		prepareBookingsMap(); /***** Prepare Bookings Map before checking env avaliability ****/
		if (!isAvailable(getModel().getEnvName(), true)) {
			getModel().setMessage("CONFIG is already booked for requested dates.");
		} else {
			boolean isUpdated = ConfigBookingFileOperations.getInstance().updateBooking(getModel());
			if (isUpdated) {	
				setSuccess(true);
				sendUpdateBookingMail();				
			} else {
				getModel().setMessage("Unable to process request. Please try again.");
			}
		}
		return "editBooking";
	}

	private void sendUpdateBookingMail() {
		int emailStatus = sendMail(getModel().getEmailAddress(),ReviewMailType.UPDATE_BOOKING);
		if (emailStatus == EMAIL_SUCCESS) {
			getModel().setMessage("Config booking updated successfully.");			
		} else {
			getModel().setMessage("Config booking updated successfully but unable to send email.");
		}		
	}

	private void setApprovedMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RE_APPROVED);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(REB_FIELD_ENV, getModel().getEnvName());
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
			setMailtext(out.toString());
			setMailSubject(getModel().getEnvName() + " reserved from " + getModel().getFromDate() + " to " + getModel().getToDate());
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	private void setRejectMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RE_REJECT);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(REB_FIELD_REJECT_REASON, getModel().getReason());
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
			setMailtext(out.toString());
			setMailSubject("CONFIG booking request is cancelled.");
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	private void setUpdateRequestMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RE_UPDATE_REQUEST);
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
			setMailtext(out.toString());
			setMailSubject("CONFIG booking request is modified.");
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	private void setUpdateBookingMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RE_UPDATE_BOOKING);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(REB_FIELD_ENV, getModel().getEnvName());
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
			setMailtext(out.toString());
			setMailSubject("CONFIG booking has been updated.");
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	private Integer sendMail(String requesterEmail, ReviewMailType mailType) {
		EmailApi mail = null;
		String[] to = null;
		String[] cc = null;
		try {
			if (mailType.equals(ReviewMailType.APPROVED)) {
				setApprovedMailProps();
			} else if (mailType.equals(ReviewMailType.REJECTED)) {
				setRejectMailProps();
			} else if (mailType.equals(ReviewMailType.UPDATE_REQUEST)) {
				setUpdateRequestMailProps();
			} else if (mailType.equals(ReviewMailType.UPDATE_BOOKING)) {
				setUpdateBookingMailProps();
			} else {
				return EMAIL_FAILURE;
			}

			to = new String[1];
			to[0] = requesterEmail;
			String ccString = null;
			if(getModel().getNotifies() != null){
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RE_EMAIL_CC))	+ "," + getModel().getNotifies();				
			} else{
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RE_EMAIL_CC));
			}
			
			if(getModel().getRegion().equalsIgnoreCase(REGION_SINGAPORE) || getModel().getRegion().equalsIgnoreCase(REGION_SYDNEY) || getModel().getRegion().equalsIgnoreCase(REGION_TOKYO)){
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_APAC));
			} else if (getModel().getRegion().equalsIgnoreCase(REGION_NEW_YORK)) {
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_NY));
			} else {
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get("EMAIL_"+getModel().getBusiness()));
			}			
			cc = ccString.split(",");			
			
			Logger.log(this.getClass().getName(), Logger.INFO,"TO:"+Arrays.toString(to)+", CC:"+Arrays.toString(cc));

			mail = new EmailApi();
			mail.setFrom(FROM_ADDRESS);
			mail.setRecipientsTo(to);
			mail.setRecipientsCc(cc);
			mail.setSentDate(new java.util.Date());
			mail.setMimeType(EmailApi.HTML);
			mail.setSubject(getMailSubject());
			mail.setText(getMailtext());
			
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

	private boolean isValidEmail(String hex) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
	}

}
