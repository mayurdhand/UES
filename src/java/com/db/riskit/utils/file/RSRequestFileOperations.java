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
import com.db.riskit.dto.BookRSDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;

public class RSRequestFileOperations implements RiskItConstants {

	private static final RSRequestFileOperations _instance = new RSRequestFileOperations();
	private File requestFile;

	private RSRequestFileOperations() {
		try {
			requestFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_RS_REQUEST);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static RSRequestFileOperations getInstance() {
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
	
	public boolean makeBookingRequest(BookRSDTO bookRSDTO) 
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
				bookRSDTO.setLineNo(counter);
				sheet.addCell(new Label(j++, counter,bookRSDTO.getProdRS()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getProjectName()));
				sheet.addCell(new Label(j++, counter, bookRSDTO.getManager()));
				sheet.addCell(new DateTime(j++, counter,bookRSDTO.getGoLiveDateInDF(), dateFormat));
				sheet.addCell(new DateTime(j++, counter,bookRSDTO.getDecomDateInDF(), dateFormat));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getBookedBy()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getEmailAddress()));
				sheet.addCell(new Label(j++, counter, STATUS_PENDING));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getRegion()));
				/******** Coloum left for Rejection reason******/
				sheet.addCell(new Label(++j, counter, bookRSDTO.getReqSummary()));
				sheet.addCell(new Label(++j, counter, bookRSDTO.getNotifies()));
				sheet.addCell(new Label(++j, counter, bookRSDTO.getBusiness()));
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

	public ArrayList<BookRSDTO> getPendingRequestsList() {
		Workbook requestWB = null;
		Sheet requestSheet = null;
		ArrayList<BookRSDTO> rsDtoList = null;
		BookRSDTO tempRSDTO = null;
		DateCell dc = null;
		int j=0;
		synchronized (requestFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(requestFile);
				requestSheet = requestWB.getSheet(0);
				rsDtoList = new ArrayList<BookRSDTO>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if (requestSheet != null) {
					Logger.log(this.getClass().getName(), Logger.INFO, " No of records in RS Request Sheet : "+ requestSheet.getRows());
					for (int counter = 1; counter < requestSheet.getRows(); counter++) 
					{
						if (requestSheet.getCell(7, counter).getContents().equalsIgnoreCase(STATUS_PENDING)) 
						{
							tempRSDTO = new BookRSDTO();
							tempRSDTO.setLineNo(counter);
							tempRSDTO.setProdRS(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setProjectName(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setManager(requestSheet.getCell(j++,counter).getContents());
							dc = (DateCell) requestSheet.getCell(j++, counter);
							tempRSDTO.setGoLiveDate(sdf.format(dc.getDate()));
							dc = (DateCell) requestSheet.getCell(j++, counter);
							tempRSDTO.setDecomDate(sdf.format(dc.getDate()));
							tempRSDTO.setBookedBy(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setEmailAddress(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setBookingStatus(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setRegion(requestSheet.getCell(j++,counter).getContents());
							/******** Coloum left for Rejection reason******/
							tempRSDTO.setReqSummary(requestSheet.getCell(++j,counter).getContents());
							tempRSDTO.setNotifies(requestSheet.getCell(++j,counter).getContents());
							tempRSDTO.setBusiness(requestSheet.getCell(++j,counter).getContents());
							j=0;
							tempRSDTO.setLineNo(counter);
							rsDtoList.add(tempRSDTO);
						}
					}
				}
				return rsDtoList;
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
	
	public String getBookingStatus(BookRSDTO bookRSDTO) {
		Workbook requestWB = null;
		Sheet sheet = null;
		String statusFromFile = null;
		synchronized (requestFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(requestFile);
				sheet = requestWB.getSheet(0);
				statusFromFile = (sheet.getCell(10, bookRSDTO.getLineNo()).getContents());
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

	public Integer updateStatus(BookRSDTO bookRSDTO,String statusToBeUpdated) {
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
				statusFromFile = (sheet.getCell(7, bookRSDTO.getLineNo()).getContents());
				if (statusToBeUpdated.equalsIgnoreCase(STATUS_APPROVED)	&& statusFromFile.equalsIgnoreCase(STATUS_PENDING)) {
					Label statusLabel = new Label(7,bookRSDTO.getLineNo(), statusToBeUpdated);
					sheet.addCell(statusLabel);
					returnStatus = REQUEST_SUCCESS;
				} else if (statusToBeUpdated.equalsIgnoreCase(STATUS_REJECTED) && statusFromFile.equalsIgnoreCase(STATUS_PENDING)) {
					Label statusLabel = new Label(7,bookRSDTO.getLineNo(), statusToBeUpdated);
					sheet.addCell(statusLabel);
					Label remarksLabel = new Label(9,bookRSDTO.getLineNo(),bookRSDTO.getReason());
					sheet.addCell(remarksLabel);
					returnStatus = REQUEST_SUCCESS;
				}else if (statusToBeUpdated.equalsIgnoreCase(STATUS_PENDING) && statusFromFile.equalsIgnoreCase(STATUS_APPROVED)) {
					Label statusLabel = new Label(7,bookRSDTO.getLineNo(), statusToBeUpdated);
					sheet.addCell(statusLabel);
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
	
	public Integer updateRequest(BookRSDTO bookRSDTO) {
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
				int counter = bookRSDTO.getLineNo();
				/**
				 * Set Line Number of the request
				 **/
				statusFromFile = (sheet.getCell(7, counter).getContents());
				if (!statusFromFile.equalsIgnoreCase(STATUS_PENDING)) {
					returnStatus = REQUEST_ALREADY_PROCESSED;
				} else {					
					bookRSDTO.setLineNo(counter);
					sheet.addCell(new Label(j++, counter, bookRSDTO.getProdRS()));
					sheet.addCell(new Label(j++, counter, bookRSDTO.getProjectName()));
					sheet.addCell(new Label(j++, counter, bookRSDTO.getManager()));
					sheet.addCell(new DateTime(j++, counter, bookRSDTO.getGoLiveDateInDF(), dateFormat));
					sheet.addCell(new DateTime(j++, counter, bookRSDTO.getDecomDateInDF(), dateFormat));
					sheet.addCell(new Label(j++, counter, bookRSDTO.getBookedBy()));
					sheet.addCell(new Label(j++, counter, bookRSDTO.getEmailAddress()));
					sheet.addCell(new Label(j++, counter, STATUS_PENDING));
					sheet.addCell(new Label(j++, counter, bookRSDTO.getRegion()));
					/******** Coloum left for Rejection reason******/
					sheet.addCell(new Label(++j, counter, bookRSDTO.getReqSummary()));
					sheet.addCell(new Label(++j, counter, bookRSDTO.getNotifies()));
					sheet.addCell(new Label(++j, counter, bookRSDTO.getBusiness()));
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
	

	public boolean deleteRequest(BookRSDTO bookRSDTO) {
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
				sheet.removeRow(bookRSDTO.getLineNo());
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
	
}
