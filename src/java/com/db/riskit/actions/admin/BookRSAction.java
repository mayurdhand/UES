package com.db.riskit.actions.admin;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.BookRSDTO;
import com.db.riskit.enums.ReviewMailType;
import com.db.riskit.scheduler.rs.RSBookingExpieryMailScheduler;
import com.db.riskit.utils.EmailApi;
import com.db.riskit.utils.TemplateUtil;
import com.db.riskit.utils.file.RSBookingFileOperations;
import com.db.riskit.utils.file.RSRequestFileOperations;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.mail.smtp.SMTPAddressFailedException;

import freemarker.template.Template;

public class BookRSAction extends RiskItActionSupport implements ModelDriven<BookRSDTO> {
	
	private static final long serialVersionUID = 1L;
	private BookRSDTO bookRSDTO;
	private List<BookRSDTO> bookRSDTOList;
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

	public BookRSDTO getBookRSDTO() {
		return bookRSDTO;
	}

	public void setBookRSDTO(BookRSDTO bookRSDTO) {
		this.bookRSDTO = bookRSDTO;
	}

	public List<BookRSDTO> getBookRSDTOList() {
		return bookRSDTOList;
	}

	public void setBookRSDTOList(List<BookRSDTO> bookRSDTOList) {
		this.bookRSDTOList = bookRSDTOList;
	}
	
	public BookRSDTO getModel() {
		return this.bookRSDTO;
	}

	public void prepare() throws Exception {
		super.prepare();
		prepareBookRSDTO();
	}

	private void prepareBookRSDTO() {
		if (this.bookRSDTO == null)
			this.bookRSDTO = new BookRSDTO();
	}
	
	public String start() {
		if (!RSBookingExpieryMailScheduler.getInstance().isRunning()) {
			RSBookingExpieryMailScheduler.getInstance().start();
			getModel().setMessage("RS Checker started successfully.");
		} else {
			getModel().setMessage("RS Checker is already running.");
		}
		setSuccess(true);
		return "scheduler";
	}

	public String stop() {
		if (RSBookingExpieryMailScheduler.getInstance().isRunning()) {
			RSBookingExpieryMailScheduler.getInstance().stop();
			getModel().setMessage("RS Checker stopped successfully.");
		} else {
			getModel().setMessage("RS Checker is already stopped.");
		}
		setSuccess(true);
		return "scheduler";
	}

	public String status() {
		if (RSBookingExpieryMailScheduler.getInstance().isRunning()) {
			getModel().setMessage("RS Checker is running.");
		} else {
			getModel().setMessage("RS Checker is stopped.");
		}
		setSuccess(true);
		return "scheduler";
	}
	
	public String showRSPendingDecom() {
		List<BookRSDTO> rsDtoList = null;
		rsDtoList = RSBookingFileOperations.getInstance().getPendingDecomRS();
		if (rsDtoList != null && rsDtoList.size() == 0) {
			setBookRSDTOList(null);
			getModel().setMessage("No pending requests.");
		} else {
			setBookRSDTOList(rsDtoList);
		}
		setSuccess(true);
		return "showPendingDecom";
	}
	
	public String decommission() {
		RSBookingFileOperations.getInstance().decommission(getModel().getDecomIds());
		getModel().setMessage("Report Server(s) marked as decommissioned.");
		setSuccess(true);
		return "showPendingDecom";
	}

	public String showPendingRequests() {
		List<BookRSDTO> rsDtoList = null;
		rsDtoList = RSRequestFileOperations.getInstance().getPendingRequestsList();
		if (rsDtoList != null && rsDtoList.size() == 0) {
			setBookRSDTOList(null);
			getModel().setMessage("No pending requests.");
		} else {
			setBookRSDTOList(rsDtoList);
		}
		setSuccess(true);
		return "showPendingRequests";
	}
	
	public String updateBooking() {
		if (getModel().getDecomDateInDF().compareTo(getModel().getGoLiveDateInDF()) < 0) {
			getModel().setMessage("Decommissioning Date should be greater than Go Live Date");
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
		if (getModel().getReqSummary().length() > 500) {
			getModel().setMessage("Requirement Summary should not be more than 500 characters.");
			return "showPendingRequests";
		}
		boolean isUpdated = RSBookingFileOperations.getInstance().updateBooking(getModel());
		if (isUpdated) {	
			setSuccess(true);
			sendUpdateBookingMail();				
		} else {
			getModel().setMessage("Unable to process request. Please try again.");
		}
		return "showPendingRequests";
	}
	
	private void sendUpdateBookingMail() {
		int emailStatus = sendMail(getModel().getEmailAddress(),ReviewMailType.UPDATE_BOOKING);
		if (emailStatus == EMAIL_SUCCESS) {
			getModel().setMessage("Report Server booking updated successfully.");
		} else {
			getModel().setMessage("Report Server booking updated successfully but unable to send email.");
		}
	}
	
	public String addToSchedules() {
		int requestStatus = RSRequestFileOperations.getInstance().updateStatus(getModel(), STATUS_APPROVED);
		switch (requestStatus) {
			case REQUEST_FAILURE:
				getModel().setMessage("Unable to process request. Please try again.");
				break;
			case REQUEST_SUCCESS:
				int status = RSBookingFileOperations.getInstance().addToSchedules(getModel());
				switch(status){
					case REQUEST_SUCCESS:
						sendApprovedMail();
						setSuccess(true);
						break;
					case RS_ALREADY_BOOKED:
						getModel().setMessage("RS is already booked. Please allot another RS.");
						RSRequestFileOperations.getInstance().updateStatus(getModel(),STATUS_PENDING);
						break;
					case REQUEST_FAILURE:
						getModel().setMessage("Unable to process request. Please try again.");
						RSRequestFileOperations.getInstance().updateStatus(getModel(),STATUS_PENDING);
						break;
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
			getModel().setMessage("Report Server alloted successfully.");
		} else {
			getModel().setMessage("Report Server alloted successfully but unable to send email.");
		}
	}

	public String updateRequest() {		
		if (getModel().getDecomDateInDF().compareTo(getModel().getGoLiveDateInDF()) < 0) {
			getModel().setMessage("Decommissioning Date should be greater than Go Live Date");
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
		if (getModel().getReqSummary().length() > 500) {
			getModel().setMessage("Requirement Summary should not be more than 500 characters.");
			return "showPendingRequests";
		}
		int status = RSRequestFileOperations.getInstance().updateRequest(getModel());
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
	
	public String changeRequestToRejected() {
		int	requestStatus = RSRequestFileOperations.getInstance().updateStatus(getModel(), STATUS_REJECTED);		
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
	
	private void setApprovedMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RS_APPROVED);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(RSB_FIELD_UAT_RS, getModel().getUatRS());
			rootMap.put(RSB_FIELD_PROD_RS, getModel().getProdRS());
			rootMap.put(RSB_FIELD_MANAGER, getModel().getManager());
			rootMap.put(RSB_FIELD_BUSINESS, getModel().getBusiness());
			rootMap.put(RSB_FIELD_GO_LIVE_DATE, getModel().getGoLiveDate());
			rootMap.put(RSB_FIELD_DECOM_DATE, getModel().getDecomDate());
			rootMap.put(RSB_FIELD_BOOKED_BY, getModel().getBookedBy());
			rootMap.put(RSB_FIELD_EMAIL, getModel().getEmailAddress());
			rootMap.put(RSB_FIELD_PROJECT, getModel().getProjectName());
			rootMap.put(RSB_FIELD_REGION, getModel().getRegion());
			rootMap.put(RSB_FIELD_SUMMARY, getModel().getReqSummary());			
			Writer out = new StringWriter();
			template.process(rootMap, out);	
			setMailtext(out.toString());
			setMailSubject(getModel().getUatRS() +" has been alloted for your testing");
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}
	
	private void setUpdateRequestMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RS_UPDATE_REQUEST);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(RSB_FIELD_PROD_RS, getModel().getProdRS());
			rootMap.put(RSB_FIELD_MANAGER, getModel().getManager());
			rootMap.put(RSB_FIELD_BUSINESS, getModel().getBusiness());
			rootMap.put(RSB_FIELD_GO_LIVE_DATE, getModel().getGoLiveDate());
			rootMap.put(RSB_FIELD_DECOM_DATE, getModel().getDecomDate());
			rootMap.put(RSB_FIELD_BOOKED_BY, getModel().getBookedBy());
			rootMap.put(RSB_FIELD_EMAIL, getModel().getEmailAddress());
			rootMap.put(RSB_FIELD_PROJECT, getModel().getProjectName());
			rootMap.put(RSB_FIELD_REGION, getModel().getRegion());
			rootMap.put(RSB_FIELD_SUMMARY, getModel().getReqSummary());		
			Writer out = new StringWriter();
			template.process(rootMap, out);	
			setMailtext(out.toString());
			setMailSubject("Report Server booking request is modified.");
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}		
	}
	
	private void setRejectMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RS_REJECT);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(RSB_FIELD_REJECT_REASON, getModel().getReason());
			rootMap.put(RSB_FIELD_PROD_RS, getModel().getProdRS());
			rootMap.put(RSB_FIELD_MANAGER, getModel().getManager());
			rootMap.put(RSB_FIELD_BUSINESS, getModel().getBusiness());
			rootMap.put(RSB_FIELD_GO_LIVE_DATE, getModel().getGoLiveDate());
			rootMap.put(RSB_FIELD_DECOM_DATE, getModel().getDecomDate());
			rootMap.put(RSB_FIELD_BOOKED_BY, getModel().getBookedBy());
			rootMap.put(RSB_FIELD_EMAIL, getModel().getEmailAddress());
			rootMap.put(RSB_FIELD_PROJECT, getModel().getProjectName());
			rootMap.put(RSB_FIELD_REGION, getModel().getRegion());
			rootMap.put(RSB_FIELD_SUMMARY, getModel().getReqSummary());			
			Writer out = new StringWriter();
			template.process(rootMap, out);	
			setMailtext(out.toString());
			setMailSubject("Report Server booking request is cancelled.");
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}	
	}
	
	private void setUpdateBookingMailProps() {
		try{
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RS_UPDATE_BOOKING);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(RSB_FIELD_UAT_RS, getModel().getUatRS());
			rootMap.put(RSB_FIELD_PROD_RS, getModel().getProdRS());
			rootMap.put(RSB_FIELD_MANAGER, getModel().getManager());
			rootMap.put(RSB_FIELD_BUSINESS, getModel().getBusiness());
			rootMap.put(RSB_FIELD_GO_LIVE_DATE, getModel().getGoLiveDate());
			rootMap.put(RSB_FIELD_DECOM_DATE, getModel().getDecomDate());
			rootMap.put(RSB_FIELD_BOOKED_BY, getModel().getBookedBy());
			rootMap.put(RSB_FIELD_EMAIL, getModel().getEmailAddress());
			rootMap.put(RSB_FIELD_PROJECT, getModel().getProjectName());
			rootMap.put(RSB_FIELD_REGION, getModel().getRegion());
			rootMap.put(RSB_FIELD_SUMMARY, getModel().getReqSummary());					
			Writer out = new StringWriter();
			template.process(rootMap, out);	
			setMailtext(out.toString());
			setMailSubject("Report Server booking is modified.");
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
			String ccString =null;
			if(getModel().getNotifies() != null){
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RS_EMAIL_CC))+ "," + getModel().getNotifies();
			}else {
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RS_EMAIL_CC));
			}
			
			if(getModel().getRegion().equalsIgnoreCase(REGION_SINGAPORE) || getModel().getRegion().equalsIgnoreCase(REGION_SYDNEY) ||  getModel().getRegion().equalsIgnoreCase(REGION_TOKYO)){
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
