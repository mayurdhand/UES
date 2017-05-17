package com.db.riskit.scheduler.rs;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.BookRSDTO;
import com.db.riskit.utils.EmailApi;
import com.db.riskit.utils.TemplateUtil;
import com.db.riskit.utils.file.RSBookingFileOperations;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;
import com.sun.mail.smtp.SMTPAddressFailedException;

import freemarker.template.Template;

public class RSBookingExpieryMailScheduler {

	private Timer timer;
	private static final RSBookingExpieryMailScheduler _instance = new RSBookingExpieryMailScheduler();
	private boolean isRunning = false;

	private RSBookingExpieryMailScheduler() {

	}

	public static RSBookingExpieryMailScheduler getInstance() {
		return _instance;
	}

	public boolean isRunning() {
		return isRunning;
	}

	private void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public synchronized void stop() {
		Logger.log(this.getClass().getName(), Logger.INFO, "Stopping RSBookingExpieryMailScheduler..");
		if (isRunning) {
			setRunning(false);
			this.timer.cancel();
		}
	}

	public synchronized void start() {
		Logger.log(this.getClass().getName(), Logger.INFO, "Starting RSBookingExpieryMailScheduler..");
		if (!isRunning()) {
			setRunning(true);
			Calendar date = Calendar.getInstance();
			date.set(Calendar.HOUR_OF_DAY, 13);
			date.set(Calendar.MINUTE, 58);

			timer = new Timer();
			timer.schedule(new Alerts(), date.getTime(), 1000 * 60 * 60 * 24);
			// timer.schedule(new Alerts(), 0, 60000); //testing
		}
	}

}

class Alerts extends TimerTask implements RiskItConstants{

	public void run() {
		Logger.log(this.getClass().getName(), Logger.INFO, " Checking expired RS bookings and sending alerts..");
		sendAlert();
	}

	public void sendAlert() {
		try {
			for (BookRSDTO bookRSDTO : RSBookingFileOperations.getInstance().getRSExpiring()) {
				Logger.log(this.getClass().getName(), Logger.INFO," Report Server : "+ bookRSDTO.getUatRS());
				sendMail(bookRSDTO);
			}
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	
	public int sendMail(BookRSDTO bookRSDTO) {
		EmailApi mail = null;
		String[] to = null;
		String[] cc = null;
		try {
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RS_EXPIRY);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(RSB_FIELD_UAT_RS, bookRSDTO.getUatRS());
			rootMap.put(RSB_FIELD_PROD_RS, bookRSDTO.getProdRS());
			rootMap.put(RSB_FIELD_MANAGER, bookRSDTO.getManager());
			rootMap.put(RSB_FIELD_BUSINESS, bookRSDTO.getBusiness());
			rootMap.put(RSB_FIELD_GO_LIVE_DATE, bookRSDTO.getGoLiveDate());
			rootMap.put(RSB_FIELD_DECOM_DATE, bookRSDTO.getDecomDate());
			rootMap.put(RSB_FIELD_BOOKED_BY, bookRSDTO.getBookedBy());
			rootMap.put(RSB_FIELD_EMAIL, bookRSDTO.getEmailAddress());
			rootMap.put(RSB_FIELD_PROJECT, bookRSDTO.getProjectName());
			rootMap.put(RSB_FIELD_REGION, bookRSDTO.getRegion());
			rootMap.put(RSB_FIELD_SUMMARY, bookRSDTO.getReqSummary());					
			Writer out = new StringWriter();
			template.process(rootMap, out);	

			to = new String[1];
			to[0] = bookRSDTO.getEmailAddress();
			String ccString = null;
			if(bookRSDTO.getNotifies() != null){
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RS_EMAIL_CC))	+ "," + bookRSDTO.getNotifies();				
			} else{
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RS_EMAIL_CC));
			}
			
			if(bookRSDTO.getRegion().equalsIgnoreCase(REGION_SINGAPORE) || bookRSDTO.getRegion().equalsIgnoreCase(REGION_SYDNEY) ||  bookRSDTO.getRegion().equalsIgnoreCase(REGION_TOKYO)){
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_APAC));
			} else if (bookRSDTO.getRegion().equalsIgnoreCase(REGION_NEW_YORK)) {
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_NY));
			} else {
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get("EMAIL_"+bookRSDTO.getBusiness()));
			}			
			cc = ccString.split(",");
			
			Logger.log(this.getClass().getName(), Logger.INFO,"TO:"+Arrays.toString(to)+", CC:"+Arrays.toString(cc));

			mail = new EmailApi();
			mail.setFrom(FROM_ADDRESS);
			mail.setRecipientsTo(to);
			mail.setRecipientsCc(cc);
			mail.setSentDate(new java.util.Date());
			mail.setMimeType(EmailApi.HTML);
			mail.setSubject(bookRSDTO.getUatRS() + " booking is expiring on "+ bookRSDTO.getDecomDate());
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

}
