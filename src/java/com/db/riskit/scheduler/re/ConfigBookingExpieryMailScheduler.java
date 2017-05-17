package com.db.riskit.scheduler.re;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.BookConfigDTO;
import com.db.riskit.utils.EmailApi;
import com.db.riskit.utils.TemplateUtil;
import com.db.riskit.utils.file.ConfigBookingFileOperations;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;
import com.sun.mail.smtp.SMTPAddressFailedException;

import freemarker.template.Template;

public class ConfigBookingExpieryMailScheduler {

	private Timer timer;
	private static final ConfigBookingExpieryMailScheduler _instance = new ConfigBookingExpieryMailScheduler();
	private boolean isRunning = false;

	private ConfigBookingExpieryMailScheduler() {

	}

	public static ConfigBookingExpieryMailScheduler getInstance() {
		return _instance;
	}

	public boolean isRunning() {
		return isRunning;
	}

	private void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public synchronized void stop() {
		Logger.log(this.getClass().getName(), Logger.INFO, "Stopping ConfigBookingExpieryMailScheduler..");
		if (isRunning) {
			setRunning(false);
			this.timer.cancel();
		}
	}

	public synchronized void start() {
		Logger.log(this.getClass().getName(), Logger.INFO, "Starting ConfigBookingExpieryMailScheduler..");
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
		Logger.log(this.getClass().getName(), Logger.INFO, " Checking expired RE CONFIG bookings and sending alerts....");
		sendAlert();
	}

	public void sendAlert() {
		try {
			for (BookConfigDTO bcDTO : ConfigBookingFileOperations.getInstance().getConfigsExpiring()) {
				Logger.log(this.getClass().getName(), Logger.INFO," RE Config : "+ bcDTO.getEnvName());
				sendMail(bcDTO);
			}
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public int sendMail(BookConfigDTO bcDTO) {
		EmailApi mail = null;
		String[] to = null;
		String[] cc = null;
		try {
			Template template = TemplateUtil.getTemplate(FILE_EMAIL_TEMPLATE_RE_EXPIRY);
			Map<String, String> rootMap = new HashMap<String, String>();
			rootMap.put(REB_FIELD_ENV, bcDTO.getEnvName());
			rootMap.put(REB_FIELD_PROJECT, bcDTO.getProjectName());
			rootMap.put(REB_FIELD_MANAGER, bcDTO.getManager());
			rootMap.put(REB_FIELD_BUSINESS, bcDTO.getBusiness());
			rootMap.put(REB_FIELD_FROM_DATE, bcDTO.getFromDate());
			rootMap.put(REB_FIELD_TO_DATE, bcDTO.getToDate());
			rootMap.put(REB_FIELD_BOOKED_BY, bcDTO.getBookedBy());
			rootMap.put(REB_FIELD_BA_CONTACT, bcDTO.getBaContact());
			rootMap.put(REB_FIELD_QA_CONTACT, bcDTO.getQaContact());
			rootMap.put(REB_FIELD_SENIOR_STAKEHOLDER, bcDTO.getStakeholder());
			rootMap.put(REB_FIELD_DEV_CONTACT, bcDTO.getDevContact());
			rootMap.put(REB_FIELD_EMAIL, bcDTO.getEmailAddress());
			rootMap.put(REB_FIELD_REGION, bcDTO.getRegion());
			rootMap.put(REB_FIELD_SUMMARY, bcDTO.getReqSummary());			
			Writer out = new StringWriter();
			template.process(rootMap, out);	
			
			to = new String[1];
			to[0] = bcDTO.getEmailAddress();
			String ccString = null;
			if(bcDTO.getNotifies() != null){
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RE_EMAIL_CC))	+ "," + bcDTO.getNotifies();				
			} else{
				ccString = ((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_RE_EMAIL_CC));
			}
			
			if(bcDTO.getRegion().equalsIgnoreCase(REGION_SINGAPORE) || bcDTO.getRegion().equalsIgnoreCase(REGION_SYDNEY) ||  bcDTO.getRegion().equalsIgnoreCase(REGION_TOKYO)){
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_APAC));
			} else if (bcDTO.getRegion().equalsIgnoreCase(REGION_NEW_YORK)) {
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get(PROPERTY_EMAIL_NY));
			} else {
				ccString = ccString+","+((String) PropertyConfigurationFactory.getInstance().get("EMAIL_"+bcDTO.getBusiness()));
			}			
			cc = ccString.split(",");
			
			Logger.log(this.getClass().getName(), Logger.INFO,"TO:"+Arrays.toString(to)+", CC:"+Arrays.toString(cc));

			mail = new EmailApi();
			mail.setFrom(FROM_ADDRESS);
			mail.setRecipientsTo(to);
			mail.setRecipientsCc(cc);
			mail.setSentDate(new java.util.Date());
			mail.setMimeType(EmailApi.HTML);
			mail.setSubject(bcDTO.getEnvName() + " booking is expiring on "	+ bcDTO.getToDate());
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
