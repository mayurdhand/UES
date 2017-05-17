package com.db.riskit.utils.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

public class RSBookingFileOperations implements RiskItConstants {

	private static final RSBookingFileOperations _instance = new RSBookingFileOperations();
	private File rsBookingFile;

	private RSBookingFileOperations() {
		try {
			rsBookingFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_RS_BOOKING);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static RSBookingFileOperations getInstance() {
		return _instance;
	}

	private Date getCurrentDate() 
	{
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

	public int addToSchedules(BookRSDTO bookRSDTO) 
	{
		Workbook schedulesWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		int nextRow;
		int j=0;
		int flag=0;
		synchronized (rsBookingFile) 
		{
			try 
			{
				WritableCellFormat dateFormat = new WritableCellFormat(	new jxl.write.DateFormat("dd-MM-yyyy"));
				schedulesWB = Workbook.getWorkbook(rsBookingFile);
				copy = Workbook.createWorkbook(rsBookingFile, schedulesWB);
				sheet = copy.getSheet(0);
				nextRow = sheet.getRows();
				Logger.log(this.getClass().getName(), Logger.INFO, "Record to be added after row : "+ nextRow);
				for(int k=1;k<nextRow;k++){
					if(bookRSDTO.getRegion().equals(sheet.getCell(9,k).getContents())){						
						if(sheet.getCell(0,k).getContents().equals(bookRSDTO.getUatRS())){
							flag =1;
						}
					}
				}
				if(flag ==1){
					return RS_ALREADY_BOOKED;
				} else {
					sheet.addCell(new Label(j++, nextRow, bookRSDTO.getUatRS()));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getProdRS()));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getProjectName()));
					sheet.addCell(new Label(j++, nextRow, bookRSDTO.getManager()));
					sheet.addCell(new DateTime(j++, nextRow,bookRSDTO.getGoLiveDateInDF(), dateFormat));
					sheet.addCell(new DateTime(j++, nextRow,bookRSDTO.getDecomDateInDF(), dateFormat));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getBookedBy()));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getEmailAddress()));
					sheet.addCell(new Label(j++, nextRow, STATUS_APPROVED));
					sheet.addCell(new Label(j++, nextRow, bookRSDTO.getRegion()));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getReqSummary()));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getNotifies()));
					sheet.addCell(new Label(j++, nextRow,bookRSDTO.getBusiness()));
					j=0;
				}
				return REQUEST_SUCCESS;
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
				close(schedulesWB);
			}
		}
		return REQUEST_FAILURE;
	}
	
	public Map<String,ArrayList<BookRSDTO>> searchRSBookings(String bookingStatus, String region) {
		Workbook requestWB = null;
		Sheet requestSheet = null;
		ArrayList<BookRSDTO> allList = null;
		ArrayList<BookRSDTO> decomList = null;
		ArrayList<BookRSDTO> pendingDecomList = null;
		ArrayList<BookRSDTO> activeList = null;
		Map<String,ArrayList<BookRSDTO>> rsbMap= new HashMap<String,ArrayList<BookRSDTO>>();
		BookRSDTO tempRSDTO = null;
		DateCell dc = null;
		int j=0;
		synchronized (rsBookingFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(rsBookingFile);
				requestSheet = requestWB.getSheet(0);
				allList = new ArrayList<BookRSDTO>();
				decomList = new ArrayList<BookRSDTO>();
				pendingDecomList = new ArrayList<BookRSDTO>();
				activeList = new ArrayList<BookRSDTO>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if (requestSheet != null) {
					for (int counter = 1; counter < requestSheet.getRows(); counter++) 
					{
						tempRSDTO = new BookRSDTO();
						tempRSDTO.setLineNo(counter);
						tempRSDTO.setUatRS(requestSheet.getCell(j++,counter).getContents());
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
						tempRSDTO.setReqSummary(requestSheet.getCell(j++,counter).getContents());
						tempRSDTO.setNotifies(requestSheet.getCell(j++,counter).getContents());
						tempRSDTO.setBusiness(requestSheet.getCell(j++,counter).getContents());
						j=0;
						tempRSDTO.setLineNo(counter);
						if(region.equalsIgnoreCase("ALL") | region.equalsIgnoreCase(tempRSDTO.getRegion())){
							allList.add(tempRSDTO);	
							if(tempRSDTO.getBookingStatus().equalsIgnoreCase(STATUS_DECOMMISSIONED)){
								decomList.add(tempRSDTO);
							} else if(tempRSDTO.getDecomDateInDF().compareTo(getCurrentDate()) < 0){
								pendingDecomList.add(tempRSDTO);
							} else if(tempRSDTO.getDecomDateInDF().compareTo(getCurrentDate()) >= 0){
								activeList.add(tempRSDTO);
							}
						}
					}
					rsbMap.put("ALL", allList);
					rsbMap.put("DECOMMISSIONED", decomList);
					rsbMap.put("PENDING DECOMMISSIONING", pendingDecomList);
					rsbMap.put("ACTIVE", activeList);
				}
				return rsbMap;
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
	
	public ArrayList<BookRSDTO> getPendingDecomRS() {
		Workbook requestWB = null;
		Sheet requestSheet = null;
		ArrayList<BookRSDTO> pendingDecomList = null;
		BookRSDTO tempRSDTO = null;
		DateCell dc = null;
		int j=0;
		synchronized (rsBookingFile) 
		{
			try 
			{
				requestWB = Workbook.getWorkbook(rsBookingFile);
				requestSheet = requestWB.getSheet(0);
				pendingDecomList = new ArrayList<BookRSDTO>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if (requestSheet != null) {
					for (int counter = 1; counter < requestSheet.getRows(); counter++) 
					{
						if(requestSheet.getCell(8,counter).getContents().equalsIgnoreCase(STATUS_APPROVED)){
							tempRSDTO = new BookRSDTO();
							tempRSDTO.setLineNo(counter);
							tempRSDTO.setUatRS(requestSheet.getCell(j++,counter).getContents());
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
							tempRSDTO.setReqSummary(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setNotifies(requestSheet.getCell(j++,counter).getContents());
							tempRSDTO.setBusiness(requestSheet.getCell(j++,counter).getContents());
							j=0;
							tempRSDTO.setLineNo(counter);
							if (tempRSDTO.getDecomDateInDF().compareTo(getCurrentDate()) < 0) {
								pendingDecomList.add(tempRSDTO);
							}
						}						
					}
				}
				return pendingDecomList;
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
	
	public boolean decommission(String commaSepLineNo) 
	{
		Workbook schedulesWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		synchronized (rsBookingFile) 
		{
			try 
			{
				schedulesWB = Workbook.getWorkbook(rsBookingFile);
				copy = Workbook.createWorkbook(rsBookingFile, schedulesWB);
				sheet = copy.getSheet(0);
				String  lineNos[] = commaSepLineNo.split(",");
				for(String tempLineNo : lineNos){
					sheet.addCell( new Label(8, Integer.parseInt(tempLineNo), STATUS_DECOMMISSIONED));
				}
				return true;
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
				close(schedulesWB);
			}
		}
		return false;
	}
	
	public boolean updateBooking(BookRSDTO bookRSDTO) 
	{
		Workbook schedulesWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		int j=0;
		synchronized (rsBookingFile) 
		{
			try 
			{
				WritableCellFormat dateFormat = new WritableCellFormat(new jxl.write.DateFormat("dd-MM-yyyy"));
				schedulesWB = Workbook.getWorkbook(rsBookingFile);
				copy = Workbook.createWorkbook(rsBookingFile, schedulesWB);
				sheet = copy.getSheet(0);
				int counter = bookRSDTO.getLineNo();
				/**
				 * Set Line Number of the request
				 **/
				bookRSDTO.setLineNo(counter);
				sheet.addCell(new Label(j++, counter, bookRSDTO.getUatRS()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getProdRS()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getProjectName()));
				sheet.addCell(new Label(j++, counter, bookRSDTO.getManager()));
				sheet.addCell(new DateTime(j++, counter,bookRSDTO.getGoLiveDateInDF(), dateFormat));
				sheet.addCell(new DateTime(j++, counter,bookRSDTO.getDecomDateInDF(), dateFormat));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getBookedBy()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getEmailAddress()));
				sheet.addCell( new Label(j++, counter, STATUS_APPROVED));
				sheet.addCell( new Label(j++, counter, bookRSDTO.getRegion()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getReqSummary()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getNotifies()));
				sheet.addCell(new Label(j++, counter,bookRSDTO.getBusiness()));
				j=0;
				return true;
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
				close(schedulesWB);
			}
		}
		return false;
	}
	
	public ArrayList<BookRSDTO> getRSExpiring() {
		Workbook rsSchedulesWB = null;
		Sheet bookingSheet = null;
		ArrayList<BookRSDTO> allList = null;
		BookRSDTO tempRSDTO = null;
		DateCell dd = null;
		DateCell gl = null;
		synchronized (rsBookingFile) 
		{
			try 
			{
				rsSchedulesWB = Workbook.getWorkbook(rsBookingFile);
				bookingSheet = rsSchedulesWB.getSheet(0);
				allList = new ArrayList<BookRSDTO>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if (bookingSheet != null) {
					for (int counter = 1; counter < bookingSheet.getRows(); counter++) 
					{
						gl = (DateCell) bookingSheet.getCell(4, counter);
						dd = (DateCell) bookingSheet.getCell(5, counter);
						Long diff = (dd.getDate().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
						Logger.log(this.getClass().getName(), Logger.INFO, " REPORT SERVER: "+ bookingSheet.getCell(0, counter).getContents() + ", DATE DIFF : "+ diff);
						if (diff <= 7 && diff >= 0){
							tempRSDTO = new BookRSDTO();
							tempRSDTO.setLineNo(counter);
							tempRSDTO.setUatRS(bookingSheet.getCell(0,counter).getContents());
							tempRSDTO.setProdRS(bookingSheet.getCell(1,counter).getContents());
							tempRSDTO.setProjectName(bookingSheet.getCell(2,counter).getContents());
							tempRSDTO.setManager(bookingSheet.getCell(3,counter).getContents());
							tempRSDTO.setGoLiveDate(sdf.format(gl.getDate()));
							tempRSDTO.setDecomDate(sdf.format(dd.getDate()));
							tempRSDTO.setBookedBy(bookingSheet.getCell(6,counter).getContents());
							tempRSDTO.setEmailAddress(bookingSheet.getCell(7,counter).getContents());
							tempRSDTO.setBookingStatus(bookingSheet.getCell(8,counter).getContents());
							tempRSDTO.setRegion(bookingSheet.getCell(9,counter).getContents());
							tempRSDTO.setReqSummary(bookingSheet.getCell(10,counter).getContents());
							tempRSDTO.setNotifies(bookingSheet.getCell(11,counter).getContents());
							tempRSDTO.setBusiness(bookingSheet.getCell(12,counter).getContents());
							tempRSDTO.setLineNo(counter);
							allList.add(tempRSDTO);	
						}
					}
				}
				return allList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(rsSchedulesWB);
			}
		}
		return null;
	}

}
