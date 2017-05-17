package com.db.riskit.utils;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPAddressFailedException;

public class EmailApi {
	private static Session sendMailSession;
	private Transport transport;
	private static Properties props = null;

	private String fromURL;
	private InternetAddress toRecipents[];
	private InternetAddress ccRecipents[];
	private String subject;
	private Date date;
	private String message_text;
	private String filename[];
	private String attachmentName[];

	public static String TEXT = "text/plain;charset=utf-8";
	public static String HTML = "text/html;charset=utf-8";
	private String sMimeType_ = TEXT;// By Default Text
	
	static {
		try {

			if (props == null) {

				props = new Properties();
				String smtpServer = "smtphub.ap.mail.db.com";

				if (smtpServer == null || smtpServer.equals(""))
					smtpServer = "localhost";

				props.put("mail.smtp.host", smtpServer);

				String smtpPort = props.getProperty("SMTPPORT");

				if (smtpPort != null) {
					try {
						Integer.parseInt(smtpPort);
						props.put("mail.smtp.port", smtpPort);
					} catch (NumberFormatException exp) {
						props.put("mail.smtp.port", "25");
					}
				} else {
					props.put("mail.smtp.port", "25");
				}
			}
		} catch (Exception exp) {
			System.out.println(" Exception in Static Block : DESMail : " + exp);
		}
	}

	private void connectToTransport() throws Exception {
		try {
			if (transport == null) {
				transport = sendMailSession.getTransport("smtp");
			}
			transport.connect();
		} catch (MessagingException me) {
			postProcess();
			sendMailSession = null;
			throw new Exception("ECO4125");
		}
	}

	public EmailApi() throws Exception {
		try {
			if (sendMailSession == null) {
				sendMailSession = Session.getInstance(props, null);
				sendMailSession.setDebug(false);
			}
			connectToTransport();
		} catch (Exception me) {
			postProcess();
			sendMailSession = null;
			throw new Exception("ECO1004");
		}
	}

	/**
	 * Set sender's URL
	 * 
	 * @param fromURL
	 *            sender's URL
	 */
	public void setFrom(String fromURL) {
		this.fromURL = fromURL;
	}

	/**
	 * Set recipents' URLs
	 * 
	 * @param fromURL
	 *            recipents' URLs
	 * @exception AddressException
	 *                thrown if the internet address is not resolved
	 */
	public void setRecipientsTo(String[] toRecipents) throws Exception {
		try {
			this.toRecipents = new InternetAddress[toRecipents.length];
			for (int i = 0; i < toRecipents.length; i++)
				this.toRecipents[i] = new InternetAddress(toRecipents[i]);
		} catch (AddressException addEx) {

			throw new Exception("ECO0100");
		}
	}

	/**
	 * Set recipents' URLs as string parsed.
	 * 
	 * @param toRecipents
	 *            recipents' URLs
	 * @exception AddressException
	 *                thrown if the internet address is not resolved
	 */
	public void setRecipientsToParse(String toRecipents) throws Exception {
		try {
			this.toRecipents = InternetAddress.parse(toRecipents);
		} catch (AddressException addEx) {

			throw new Exception("ECO0100");
		}
	}

	public void setRecipientsCcParse(String ccRecipents) throws Exception {
		try {
			if (ccRecipents != null)
				this.ccRecipents = InternetAddress.parse(ccRecipents);
		} catch (AddressException addEx) {

			throw new Exception("ECO0100");
		}
	}

	/**
	 * Set recipents' cc URLs
	 * 
	 * @param fromURL
	 *            recipents' cc URLs
	 * @exception AddressException
	 *                thrown if the internet address is not resolved
	 */
	public void setRecipientsCc(String[] ccRecipents) throws Exception {
		try {
			this.ccRecipents = new InternetAddress[ccRecipents.length];
			for (int i = 0; i < ccRecipents.length; i++)
				this.ccRecipents[i] = new InternetAddress(ccRecipents[i]);
		} catch (AddressException addEx) {

			throw new Exception("ECO0100");
		}

	}

	public Address[] getAllRecipients() {

		Vector<InternetAddress> vInternetAssignees = new Vector<InternetAddress>();

		if (this.toRecipents != null) {
			for (int i = 0; i < this.toRecipents.length; i++) {
				vInternetAssignees.add(this.toRecipents[i]);
			}
		}
		if (this.ccRecipents != null) {
			for (int i = 0; i < this.ccRecipents.length; i++) {
				vInternetAssignees.add(this.ccRecipents[i]);
			}
		}
		int iTotalSize = vInternetAssignees.size();

		InternetAddress allAddresses[] = new InternetAddress[iTotalSize];
		vInternetAssignees.toArray(allAddresses);

		return allAddresses;
	}

	/**
	 * Set subject of the mail
	 * 
	 * @param subject
	 *            subject
	 */
	public void setSubject(String subject) throws Exception {
		this.subject = subject;

		try {
			this.subject = new String(subject.getBytes(), "UTF-8");
		} catch (java.io.UnsupportedEncodingException ue) {

			throw new Exception("ECO0100");
		}
	}

	/**
	 * Set sending date of the mail
	 * 
	 * 
	 * @param date
	 *            sending date
	 */
	public void setSentDate(Date date) {
		this.date = date;
	}

	/**
	 * Set context of the mail
	 * 
	 * @param text
	 *            context of the mail
	 */
	public void setText(String text) {
		this.message_text = text;
	}

	/**
	 * Set attachment file name
	 * 
	 * @param filename
	 *            attachment file name
	 */
	public void setAttachment(String filename[]) {
		this.filename = filename;
	}

	public void setAttachmentName(String attachName[]) {
		attachmentName = attachName;
	}

	/**
	 * Send mail
	 * 
	 * @return true if sending is successful, otherwise returns false
	 */
	public boolean send(String lang) throws SMTPAddressFailedException,Exception {
		try {
			MimeMessage newMessage = new MimeMessage(sendMailSession);
			newMessage.setFrom(new InternetAddress(fromURL));
			newMessage.setRecipients(Message.RecipientType.TO, toRecipents);
			newMessage.setRecipients(Message.RecipientType.CC, ccRecipents);
			newMessage.setSubject(subject, "UTF-8");
			newMessage.setSentDate(date);
			// do we have a file to send?
			if (filename != null) {
				// yes - create the individual parts of the MIME message
				// first, create the plain text message body
				MimeBodyPart message_body = new MimeBodyPart();
				// create the Multipart message
				if (sMimeType_.equals(TEXT)) {
					message_body.setText(message_text, "UTF-8");
					// message_body.setText(message_text);
					// message_body.setHeader("Content-Type",sMimeType_);
					// message_body.setHeader("Content-Transfer-Encoding",
					// "8bit");
				} else if (sMimeType_.equals(HTML)) {
					if (message_text != null) {
						byte[] byte_arr = message_text.getBytes();
						message_text = new String(byte_arr, "UTF-8");
					}
					message_body.setContent(message_text, sMimeType_);
				}
				message_body.setHeader("Content-Language", lang);
				Multipart multipart_message = new MimeMultipart();
				// add the two bodies to this multipart
				multipart_message.addBodyPart(message_body);

				/*
				 * next, create the attachment body part.
				 */
				for (int i = 0; i < filename.length; i++) {
					DataSource file_data_source = new FileDataSource(
							filename[i]);
					DataHandler file_data_handler = new DataHandler(
							file_data_source);
					MimeBodyPart file_attachment = new MimeBodyPart();
					file_attachment.setDataHandler(file_data_handler);
					File temp_file = new File(filename[i]);

					if (attachmentName != null && attachmentName.length > i) {
						file_attachment.setFileName(attachmentName[i]);
					} else {
						file_attachment.setFileName(temp_file.getName());
					}
					multipart_message.addBodyPart(file_attachment);
				}
				// set the content type of this message to indicate multipart
				newMessage.setContent(multipart_message);
			} else {
				// no - send a plain message
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				if (sMimeType_.equals(TEXT)) {
					// messageBodyPart.setText(message_text);
					messageBodyPart.setText(message_text, "UTF-8");
					// messageBodyPart.setHeader("Content-Type",sMimeType_);
					// messageBodyPart.setHeader("Content-Transfer-Encoding",
					// "8bit");
				} else if (sMimeType_.equals(HTML)) {
					if (message_text != null) {
						byte[] byte_arr = message_text.getBytes();
						message_text = new String(byte_arr, "UTF-8");
					}
					messageBodyPart.setContent(message_text, sMimeType_);
				}
				messageBodyPart.setHeader("Content-Language", lang);
				MimeMultipart multipart = new MimeMultipart("alternative");
				multipart.addBodyPart(messageBodyPart);
				newMessage.setContent(multipart);
			}
			// transport.send(newMessage);
			transport.sendMessage(newMessage, getAllRecipients());
			return true;
		} catch (SendFailedException sae) {	
			Object objThrowable = sae.getNextException();
			if(objThrowable instanceof SMTPAddressFailedException){
				throw (SMTPAddressFailedException)objThrowable;
			}
			throw sae;
		} catch (MessagingException m) {
			m.printStackTrace();
			dumpObject();
			Object objThrowable = m.getNextException();
			if (objThrowable instanceof java.io.IOException) {
				postProcess();
				try {
					connectToTransport();
				} catch (Exception e) {
					// log.error("DESMail.send() - - "+ subject +
					// " - Exception while re-establishing the Transport Connection : "
					// + e.toString());
					throw e;
				}
				// File required for attachment in mail is missing.
				throw new Exception("ECO4124");
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			dumpObject();
			return false;
		}
	}

	public boolean sendAttachment(String lang, String filename)
			throws Exception {
		try {
			MimeMessage message = new MimeMessage(sendMailSession);
			message.setFrom(new InternetAddress(fromURL));
			message.setRecipients(Message.RecipientType.TO, toRecipents);
			message.setRecipients(Message.RecipientType.CC, ccRecipents);
			message.setSubject(subject, "UTF-8");
			message.setSentDate(date);
			MimeBodyPart messageBodyPart = new MimeBodyPart(); 
			
			// Fill the message
			messageBodyPart.setText(message_text);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(new File(filename));
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			// Send the message
			Transport.send(message);
			return true;
		} catch (MessagingException m) {
			m.printStackTrace();
			dumpObject();
			Object objThrowable = m.getNextException();
			if (objThrowable instanceof java.io.IOException) {
				postProcess();
				try {
					connectToTransport();
				} catch (Exception e) {
					// log.error("DESMail.send() - - "+ subject +
					// " - Exception while re-establishing the Transport Connection : "
					// + e.toString());
					throw e;
				}
				// File required for attachment in mail is missing.
				throw new Exception("ECO4124");
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			dumpObject();
			return false;
		}
	}

	public void setMimeType(String sMimeTypeIn) {
		this.sMimeType_ = sMimeTypeIn;
	}

//	public void setAttachment(String sURLIn) {
//		try {
//
//			String fileNameArr[] = new String[1];
//			URL objURL = new URL(sURLIn);
//			URLDataSource objURLDataSource = new URLDataSource(objURL);
//			// String errorFilePath =
//			// LDAPQuery.getSystemProperty("gbeaitmpdir");
//			String errorFilePath = "";
//			String fileName = errorFilePath + File.separator + "outagain.pdf";
//			setAttachment(fileNameArr);
//			// if (DESLog.isDebugEnabled())
//			// log.debug("Exit DESMail.setAttachment()");
//		} catch (Exception exp) {
//			// log.error("DESException in DESMail.setAttachment()" + exp);
//		}
//	}

	/**
	 * Resets the values of From RecipientsTo RecipientsCc Subject SentDate Text
	 * Attachment to null
	 **/
	public void resetValue() {
		fromURL = null;
		toRecipents = null;
		ccRecipents = null;
		subject = null;
		date = null;
		message_text = null;
		filename = null;
		attachmentName = null;
	}

	public static void main(String[] argv) {
		System.out.println("<-------------------------------------->");
		try {
			EmailApi sampleMail = new EmailApi();
			String fromAddress = "sivaji.ravipudi@db.com";
			sampleMail.setFrom(fromAddress);
			String[] to = { "sivaji.ravipudi@db.com" };

			sampleMail.setRecipientsTo(to);
			// sampleMail.setRecipientsCc(cc);
			sampleMail.setSentDate(new java.util.Date());
			sampleMail.setMimeType(EmailApi.HTML);
			sampleMail.setSubject("TESTING HTML CONTENT IN MAIL");
			sampleMail.setText("Hi,\n Mail testing.");
			String filename[] = new String[1];
			filename[0] = "C://Test//config.prop";
			// filename[1] = "/ebp/grp5/maint/ToolkitController.java";
			sampleMail.setAttachment(filename);
			// sampleMail.setAttachment("http://www.dbs.com/tools/forms/krisflyer_enrolment.pdf");
			if (sampleMail.send(""))
				System.out.println("Success");
			else
				System.out.println("Failed to send mail");
		} catch (Exception gEx) {
			System.out.println("Exception in DESMail main()" + gEx.toString());
		}
	}

	/**
	 * Close this service and terminate the connection of the transport.
	 * 
	 */
	public void postProcess() {
		try {
			if (transport != null)
				transport.close();
			// if (DESLog.isDebugEnabled())
			// log.debug("Closing Transport Connection ");
		} catch (Exception exp) {
			// log.error(" Exception in Closing Transport in DESMail.postProcess() : "+exp);
		}
	}

	public void dumpObject() {
		StringBuffer sbDebug = new StringBuffer();
		sbDebug.append(fromURL).append(" :: ").append(subject).append(" :: ");
		sbDebug.append(message_text);
		sbDebug.append(" :: Recipients To Address : ");
		if (toRecipents != null) {
			for (int i = 0; i < toRecipents.length; i++) {
				sbDebug.append(toRecipents[i]).append(",");
			}
		}
		sbDebug.append(" :: Recipients Cc Address : ");
		if (ccRecipents != null) {
			for (int i = 0; i < ccRecipents.length; i++) {
				sbDebug.append(ccRecipents[i]).append(",");
			}
		}
		if (filename != null) {
			sbDebug.append(" :: Attachemnt File names : ");
			for (int i = 0; i < filename.length; i++) {
				sbDebug.append(filename[i]).append(",");
			}
		}

	}

}
