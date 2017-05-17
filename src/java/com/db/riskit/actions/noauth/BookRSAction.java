package com.db.riskit.actions.noauth;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.db.riskit.actions.RiskItActionSupport;
import com.db.riskit.dto.BookRSDTO;
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
	private InputStream inputStream;
	private boolean success = false;

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

	public String execute() {		
		return "noauthbca";
	}

	public String bookRS() {
		if (getModel().getDecomDateInDF().compareTo(getModel().getGoLiveDateInDF()) <= 0) {
			getModel().setMessage("Decommissioning Date should be greater than Go Live Date");
			return "noauthbca";
		}
		if (!isValidEmail(getModel().getEmailAddress())) {
			getModel().setMessage("Please enter a valid email id.");
			return "noauthbca";
		}		
		if (getModel().getReqSummary().length() > 500) {
			getModel().setMessage("Testing Summary should not be more than 500 characters.");
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
		boolean isSuccess = RSRequestFileOperations.getInstance().makeBookingRequest(getModel());
		if (isSuccess) {
			sendRequestReceivedMail();
			setSuccess(true);
		} else {
			getModel().setMessage("Unable to make request. Please try again.");
		}
		return "noauthbca";
	}

	private void sendRequestReceivedMail() {
		int status = sendMail(getModel().getEmailAddress());
		if (status == EMAIL_SUCCESS) {
			getModel().setMessage("Report Server Request Recieved");
			setSuccess(true);
		} else if (status == EMAIL_FAILURE) {
			getModel().setMessage("Unable to make request. Please try again.");
			RSRequestFileOperations.getInstance().deleteRequest(getModel());
		} else if (status == INVALID_ADDRESS) {
			getModel().setMessage("Unable to make request as the email-id you provided is invalid.");
			RSRequestFileOperations.getInstance().deleteRequest(getModel());
		}
	}
	
	public String searchRSBooking() {
		try{
			Map<String,ArrayList<BookRSDTO>> rsDTOMap = null;
			List<BookRSDTO> rsDTOList = null;
			List<BookRSDTO> tempRSDTOList = new ArrayList<BookRSDTO>();
			rsDTOMap = RSBookingFileOperations.getInstance().searchRSBookings(getModel().getBookingStatus(),getModel().getRegion());
			rsDTOList = rsDTOMap.get(getModel().getBookingStatus());
			if(rsDTOList != null && !rsDTOList.isEmpty()){
				tempRSDTOList.addAll(rsDTOList);
				if(getModel().getUatRS() != null && !getModel().getUatRS().trim().isEmpty()){
					for(BookRSDTO tempDTO : rsDTOList){
						if(!getModel().getUatRS().equalsIgnoreCase(tempDTO.getUatRS())){
							tempRSDTOList.remove(tempDTO);
						}
					}
				}
				if (tempRSDTOList != null && tempRSDTOList.size() == 0) {					
					setBookRSDTOList(null);		
					getModel().setMessage("No records found.");
				} else {			
					setBookRSDTOList(tempRSDTOList);
				}
			}else{
				setBookRSDTOList(null);		
				getModel().setMessage("No records found.");
			}
			setSuccess(true);
		}catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
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
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RS_BOOKING_REQUEST);
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
				ccString = requesterEmail+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RS_EMAIL_CC))	+ "," + getModel().getNotifies();				
			} else {
				ccString = requesterEmail+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RS_EMAIL_CC));
			}			
			cc = ccString.split(",");
			
			Logger.log(this.getClass().getName(), Logger.INFO,"TO:"+Arrays.toString(to)+", CC:"+Arrays.toString(cc));
			
			mail = new EmailApi();
			mail.setFrom(FROM_ADDRESS);
			mail.setRecipientsTo(to);
			mail.setRecipientsCc(cc);
			mail.setSentDate(new java.util.Date());
			mail.setMimeType(EmailApi.HTML);
			mail.setSubject("Report Server booking request received but not yet confirmed.");
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
	
	public String downloadFile() throws Exception {
		ArrayList<BookRSDTO> bookRSDTOList = null;
		Map<String,ArrayList<BookRSDTO>> rsDTOMap = null;
		ArrayList<BookRSDTO> tempRSDTOList = new ArrayList<BookRSDTO>();
		rsDTOMap = RSBookingFileOperations.getInstance().searchRSBookings(getModel().getBookingStatus(),getModel().getRegion());
		bookRSDTOList = rsDTOMap.get(getModel().getBookingStatus());
		if(bookRSDTOList != null && !bookRSDTOList.isEmpty()){
			tempRSDTOList.addAll(bookRSDTOList);
			if(getModel().getUatRS() != null && !getModel().getUatRS().trim().isEmpty()){
				for(BookRSDTO tempDTO : bookRSDTOList){
					if(!getModel().getUatRS().equalsIgnoreCase(tempDTO.getUatRS())){
						tempRSDTOList.remove(tempDTO);
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("UAT RS,");
			sb.append("CLONED FROM,");
			sb.append("REGION,");
			sb.append("BOOKED BY,");
			sb.append("PROJECT,");
			sb.append("PROJECT MANAGER,");
			sb.append("BUSINESS,");
			sb.append("GO LIVE DATE,");
			sb.append("DECOMMISSIONING DATE,");
			sb.append("\n");
			for (BookRSDTO temp : tempRSDTOList) {
				sb.append("\""+temp.getUatRS() + "\",");
				sb.append("\""+temp.getProdRS() + "\",");
				sb.append("\""+temp.getRegion() + "\",");
				sb.append("\""+temp.getBookedBy() + "\",");
				sb.append("\""+temp.getProjectName() + "\",");
				sb.append("\""+temp.getManager() + "\",");
				sb.append("\""+temp.getBusiness() + "\",");
				sb.append("\""+temp.getGoLiveDate() + "\",");
				sb.append("\""+temp.getDecomDate() + "\",");			
				sb.append("\n");
			}
			setInputStream(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
		}
		return "downloadFile";
	}	

}
