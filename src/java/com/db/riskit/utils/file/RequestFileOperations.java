package com.db.riskit.utils.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.BookConfigDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;

public class RequestFileOperations implements RiskItConstants{
	
	private static final RequestFileOperations _instance = new RequestFileOperations();
	private File requestFile;

	private RequestFileOperations() 
	{
		try 
		{
			requestFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_RE_REQUEST);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static RequestFileOperations getInstance() 
	{
		return _instance;
	}

	private void close(Object wb) 
	{
		if (wb == null) {
			return;
		} else if (wb instanceof Workbook) {
			((Workbook) wb).close();
		} else if (wb instanceof WritableWorkbook) {
			try {
				((WritableWorkbook) wb).close();
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			}
		}
	}
	
	private void write(WritableWorkbook copy) 
	{
		if (copy == null) {
			return;
		}
		try {
			copy.write();
		} catch (IOException e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public boolean makeBookingRequest(BookConfigDTO bookConfigDTO) 
	{
		Workbook requestWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		int j=0;
		synchronized (requestFile) 
		{
			try 
			{				
				WritableCellFormat dateFormat = new WritableCellFormat(new jxl.write.DateFormat("dd-MM-yyyy"));
				requestWB = Workbook.getWorkbook(requestFile);
				copy = Workbook.createWorkbook(requestFile, requestWB);
				sheet = copy.getSheet(0);
				int counter = sheet.getRows();
				/**
				 * Set Line Number of the request
				 **/
				bookConfigDTO.setLineNo(counter);
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getProjectName()));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getBaContact()));
				sheet.addCell(new Label(j++, counter, bookConfigDTO.getManager()));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getDevContact()));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getQaContact()));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getStakeholder()));
				sheet.addCell(new DateTime(j++, counter,bookConfigDTO.getFromDateInDF(), dateFormat));
				sheet.addCell(new DateTime(j++, counter,bookConfigDTO.getToDateInDF(), dateFormat));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getBookedBy()));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getEmailAddress()));
				sheet.addCell(new Label(j++, counter, STATUS_PENDING));
				sheet.addCell(new Label(j++, counter,bookConfigDTO.getRegion()));
				/******** Coloum 12 left for Rejection reason******/
				sheet.addCell(new Label(++j, counter, bookConfigDTO.getReqSummary()));
				sheet.addCell(new Label(++j, counter, bookConfigDTO.getNotifies()));
				sheet.addCell(new Label(++j, counter, bookConfigDTO.getBusiness()));
				j=0;
				return true;
			} catch (RowsExceededException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(requestWB);
			}
		}
		return false;
	}

	public ArrayList<BookConfigDTO> getPendingRequestsList() {
		Workbook requestWB = null;
		Sheet requestSheet = null;
		ArrayList<BookConfigDTO> bcDtoList = null;
		BookConfigDTO tempBcDTO = null;
		DateCell dc = null;
		int j=0;
		synchronized (requestFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(requestFile);
				requestSheet = requestWB.getSheet(0);
				bcDtoList = new ArrayList<BookConfigDTO>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if (requestSheet != null) {
					Logger.log(this.getClass().getName(), Logger.INFO, " No of records in RE Request Sheet : "+ requestSheet.getRows());
					for (int counter = 1; counter < requestSheet.getRows(); counter++) 
					{
						if (requestSheet.getCell(10, counter).getContents().equalsIgnoreCase(STATUS_PENDING)) 
						{
							tempBcDTO = new BookConfigDTO();
							tempBcDTO.setLineNo(counter);
							tempBcDTO.setProjectName(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setBaContact(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setManager(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setDevContact(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setQaContact(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setStakeholder(requestSheet.getCell(j++,counter).getContents());
							dc = (DateCell) requestSheet.getCell(j++, counter);
							tempBcDTO.setFromDate(sdf.format(dc.getDate()));
							dc = (DateCell) requestSheet.getCell(j++, counter);
							tempBcDTO.setToDate(sdf.format(dc.getDate()));
							tempBcDTO.setBookedBy(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setEmailAddress(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setBookingStatus(requestSheet.getCell(j++,counter).getContents());
							tempBcDTO.setRegion(requestSheet.getCell(j++,counter).getContents());
							/******** Coloum 12 left for Rejection reason******/
							tempBcDTO.setReqSummary(requestSheet.getCell(++j,counter).getContents());
							tempBcDTO.setNotifies(requestSheet.getCell(++j,counter).getContents());
							tempBcDTO.setBusiness(requestSheet.getCell(++j,counter).getContents());
							j=0;
							tempBcDTO.setLineNo(counter);
							bcDtoList.add(tempBcDTO);
						}
					}
				}
				return bcDtoList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(requestWB);
			}
		}
		return null;
	}

	public String getBookingStatus(BookConfigDTO bookConfigDTO) {
		Workbook requestWB = null;
		Sheet sheet = null;
		String statusFromFile = null;
		synchronized (requestFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(requestFile);
				sheet = requestWB.getSheet(0);
				statusFromFile = (sheet.getCell(10, bookConfigDTO.getLineNo()).getContents());
				requestWB.close();
				return statusFromFile;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(requestWB);
			}
		}
		return null;
	}

	public Integer updateStatus(BookConfigDTO bookConfigDTO,String statusToBeUpdated) {
		Workbook requestWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		String statusFromFile = null;
		int returnStatus = REQUEST_FAILURE;
		synchronized (requestFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(requestFile);
				copy = Workbook.createWorkbook(requestFile, requestWB);
				sheet = copy.getSheet(0);
				statusFromFile = (sheet.getCell(10, bookConfigDTO.getLineNo()).getContents());
				if (statusToBeUpdated.equalsIgnoreCase(STATUS_APPROVED)	&& statusFromFile.equalsIgnoreCase(STATUS_PENDING)) {
					Label statusLabel = new Label(10,bookConfigDTO.getLineNo(), statusToBeUpdated);
					sheet.addCell(statusLabel);
					returnStatus = REQUEST_SUCCESS;
				} else if (statusToBeUpdated.equalsIgnoreCase(STATUS_REJECTED) && statusFromFile.equalsIgnoreCase(STATUS_PENDING)) {
					Label statusLabel = new Label(10,bookConfigDTO.getLineNo(), statusToBeUpdated);
					sheet.addCell(statusLabel);
					Label remarksLabel = new Label(12,bookConfigDTO.getLineNo(),bookConfigDTO.getReason());
					sheet.addCell(remarksLabel);
					returnStatus = REQUEST_SUCCESS;
				} else {
					returnStatus = REQUEST_ALREADY_PROCESSED;
				}
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (RowsExceededException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(requestWB);
			}
		}
		return returnStatus;
	}
	
	public Integer updateRequest(BookConfigDTO bookConfigDTO) {
		Workbook requestWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		String statusFromFile = null;
		int returnStatus = REQUEST_FAILURE;
		int j=0;
		synchronized (requestFile) 
		{
			try 
			{
				WritableCellFormat dateFormat = new WritableCellFormat(new jxl.write.DateFormat("dd-MM-yyyy"));
				requestWB = Workbook.getWorkbook(requestFile);
				copy = Workbook.createWorkbook(requestFile, requestWB);
				sheet = copy.getSheet(0);
				int counter = bookConfigDTO.getLineNo();
				/**
				 * Set Line Number of the request
				 **/
				statusFromFile = (sheet.getCell(10, counter).getContents());
				if (!statusFromFile.equalsIgnoreCase(STATUS_PENDING)) {
					returnStatus = REQUEST_ALREADY_PROCESSED;
				} else {					
					bookConfigDTO.setLineNo(counter);
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getProjectName()));
					sheet.addCell(new Label(j++, counter,bookConfigDTO.getBaContact()));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getManager()));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getDevContact()));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getQaContact()));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getStakeholder()));
					sheet.addCell(new DateTime(j++, counter, bookConfigDTO.getFromDateInDF(), dateFormat));
					sheet.addCell(new DateTime(j++, counter, bookConfigDTO.getToDateInDF(), dateFormat));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getBookedBy()));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getEmailAddress()));
					sheet.addCell(new Label(j++, counter, STATUS_PENDING));
					sheet.addCell(new Label(j++, counter, bookConfigDTO.getRegion()));
					/******** Coloum 12 left for Rejection reason******/
					sheet.addCell(new Label(++j, counter, bookConfigDTO.getReqSummary()));
					sheet.addCell(new Label(++j, counter, bookConfigDTO.getNotifies()));
					sheet.addCell(new Label(++j, counter, bookConfigDTO.getBusiness()));
					j=0;
					returnStatus = REQUEST_SUCCESS;
				} 
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (RowsExceededException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (WriteException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(requestWB);			
			}
		}
		return returnStatus;
	}

	public boolean deleteRequest(BookConfigDTO bookConfigDTO) {
		Workbook requestWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		synchronized (requestFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(requestFile);
				copy = Workbook.createWorkbook(requestFile, requestWB);
				sheet = copy.getSheet(0);
				sheet.removeRow(bookConfigDTO.getLineNo());
				return true;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				write(copy);
				close(copy);
				close(requestWB);
			}
		}
		return false;
	}
	
	public static void main(String[] args) {	
//		BookConfigDTO b = new BookConfigDTO();
//		b.setProjectName("l");
//		b.setBaContact("l");
//		RequestFileOperations.getInstance().makeBookingRequest(b);
	}
}
