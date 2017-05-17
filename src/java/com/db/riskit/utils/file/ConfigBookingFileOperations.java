package com.db.riskit.utils.file;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.db.riskit.dto.BookConfigDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;

public class ConfigBookingFileOperations implements RiskItConstants {

	private static final ConfigBookingFileOperations _instance = new ConfigBookingFileOperations();
	private File configBookingFile;

	private ConfigBookingFileOperations() {
		try {
			configBookingFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_RE_BOOKING);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static ConfigBookingFileOperations getInstance() {
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

	public ArrayList<BookConfigDTO> getBookedEnv(BookConfigDTO bcDTO) 
	{
		Workbook bookingWb = null;
		Sheet bookingSheet = null;
		ArrayList<BookConfigDTO> bcDtoList = null;
		BookConfigDTO tempBcDTO = null;
		Date sdfFrom = null;
		Date sdfTo = null;
		DateCell dc = null;
		int j=0;
		synchronized (configBookingFile) 
		{
			try 
			{
				bookingWb = Workbook.getWorkbook(configBookingFile);
				bookingSheet = bookingWb.getSheet(0);
				bcDtoList = new ArrayList<BookConfigDTO>();
				DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String[] envArr = bcDTO.getEnvName().split(",");				
				for(int i=0;i<envArr.length;i++){
					envArr[i] = envArr[i].trim();					
				}
				Arrays.sort(envArr);
				if (bookingSheet != null) {
					Logger.log(this.getClass().getName(), Logger.INFO, " No of records in RE Booking Sheet : "+ bookingSheet.getRows());
					for (int counter = 1; counter < bookingSheet.getRows(); counter++) {
						if (Arrays.binarySearch(envArr, "ALL")>=0 
								|| Arrays.binarySearch(envArr, bookingSheet.getCell(0, counter).getContents()) >=0 ) 
						{
							sdfFrom = sdf.parse(bookingSheet.getCell(7, counter).getContents());
							sdfTo = sdf.parse(bookingSheet.getCell(8, counter).getContents());
							if (!bcDTO.getShowExpiredBookings()	
									| (bcDTO.getShowExpiredBookings() && sdfTo.compareTo(getCurrentDate()) < 0))
							{
								if ((bcDTO.getFromDateInDF().compareTo(sdfFrom) >= 0 && bcDTO.getFromDateInDF().compareTo(sdfTo) <= 0)
										|| (bcDTO.getToDateInDF().compareTo(sdfFrom) >= 0 && bcDTO.getToDateInDF().compareTo(sdfTo) <= 0)
										|| (bcDTO.getFromDateInDF().compareTo(sdfFrom) <= 0 && bcDTO.getToDateInDF().compareTo(sdfTo) >= 0)) 
								{
									tempBcDTO = new BookConfigDTO();
									tempBcDTO.setLineNo(counter);
									String envName = bookingSheet.getCell(j++, counter).getContents();	
									String region = envName.split(":")[0];
									tempBcDTO.setEnvName(envName);
									tempBcDTO.setRegion(region);
									tempBcDTO.setBaContact(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setManager(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setDevContact(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setQaContact(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setStakeholder(bookingSheet.getCell(j++, counter).getContents());
									dc = (DateCell) bookingSheet.getCell(j++,	counter);
									tempBcDTO.setFromDate(sdf.format(dc.getDate()));
									dc = (DateCell) bookingSheet.getCell(j++, counter);
									tempBcDTO.setToDate(sdf.format(dc.getDate()));
									tempBcDTO.setBookedBy(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setEmailAddress(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setBookingStatus(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setReqSummary(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setNotifies(bookingSheet.getCell(j++, counter).getContents());
									tempBcDTO.setBusiness(bookingSheet.getCell(j++, counter).getContents());
									j=0; //Reset Countor
									bcDtoList.add(tempBcDTO);
								}
							}
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
				close(bookingWb);
			}
		}
		return null;
	}

	public ArrayList<BookConfigDTO> getBookedEnvWithoutDates(BookConfigDTO bcDTO) 
	{
		Workbook bookingWb = null;
		Sheet bookingSheet = null;
		ArrayList<BookConfigDTO> bcDtoList = null;
		BookConfigDTO tempBcDTO = null;
		Date sdfTo = null;
		DateCell dc = null;
		int j=0;
		synchronized (configBookingFile) 
		{
			try 
			{
				bookingWb = Workbook.getWorkbook(configBookingFile);
				bookingSheet = bookingWb.getSheet(0);				
				bcDtoList = new ArrayList<BookConfigDTO>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String[] envArr = bcDTO.getEnvName().split(",");
				for(int i=0;i<envArr.length;i++){
					envArr[i] = envArr[i].trim();					
				}
				Arrays.sort(envArr);				
				if (bookingSheet != null) 
				{
					Logger.log(this.getClass().getName(), Logger.INFO, " No of records in RE Booking Sheet : "+ bookingSheet.getRows());
					for (int counter = 1; counter < bookingSheet.getRows(); counter++) 
					{
						if (Arrays.binarySearch(envArr, "ALL")>=0 
								|| Arrays.binarySearch(envArr,bookingSheet.getCell(0, counter).getContents()) >=0 ) 
						{							
							sdfTo = sdf.parse(bookingSheet.getCell(8, counter).getContents());
							if ((!bcDTO.getShowExpiredBookings() && sdfTo.compareTo(getCurrentDate()) >= 0)
									| (bcDTO.getShowExpiredBookings() && sdfTo.compareTo(getCurrentDate()) < 0)) 
							{
								tempBcDTO = new BookConfigDTO();
								tempBcDTO.setLineNo(counter);
								String envName = bookingSheet.getCell(j++, counter).getContents();	
								String region = envName.split(":")[0];
								tempBcDTO.setEnvName(envName);
								tempBcDTO.setRegion(region);
								tempBcDTO.setProjectName(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setBaContact(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setManager(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setDevContact(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setQaContact(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setStakeholder(bookingSheet.getCell(j++, counter).getContents());
								dc = (DateCell) bookingSheet.getCell(j++, counter);
								tempBcDTO.setFromDate(sdf.format(dc.getDate()));
								dc = (DateCell) bookingSheet.getCell(j++, counter);
								tempBcDTO.setToDate(sdf.format(dc.getDate()));
								tempBcDTO.setBookedBy(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setEmailAddress(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setBookingStatus(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setReqSummary(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setNotifies(bookingSheet.getCell(j++, counter).getContents());
								tempBcDTO.setBusiness(bookingSheet.getCell(j++, counter).getContents());
								j=0; //Reset Countor
								bcDtoList.add(tempBcDTO);

							}
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
				close(bookingWb);
			}
		}
		return null;
	}

	public Map<String, ArrayList<BookConfigDTO>> prepareBookingsMap() 
	{
		String env = null;
		Workbook bookingWb = null;
		Sheet bookingSheet = null;
		DateCell dc = null;
		BookConfigDTO bcDTO = null;
		int j=0;
		synchronized (configBookingFile) 
		{
			try 
			{
				bookingWb = Workbook.getWorkbook(configBookingFile);
				bookingSheet = bookingWb.getSheet(0);
				Map<String, ArrayList<BookConfigDTO>> bookingMap = new HashMap<String, ArrayList<BookConfigDTO>>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				if (bookingSheet != null) 
				{
					for (int counter = 1; counter < bookingSheet.getRows(); counter++) 
					{
						env = bookingSheet.getCell(0, counter).getContents();
						if (bookingMap.get(env) == null) {
							bookingMap.put(env, new ArrayList<BookConfigDTO>());
						}
						bcDTO = new BookConfigDTO();
						bcDTO.setLineNo(counter);
						String envName = bookingSheet.getCell(j++, counter).getContents();	
						String region = envName.split(":")[0];
						bcDTO.setEnvName(envName);
						bcDTO.setRegion(region);
						bcDTO.setProjectName(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setBaContact(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setManager(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setDevContact(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setQaContact(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setStakeholder(bookingSheet.getCell(j++, counter).getContents());
						dc = (DateCell) bookingSheet.getCell(j++, counter);
						bcDTO.setFromDate(sdf.format(dc.getDate()));
						dc = (DateCell) bookingSheet.getCell(j++, counter);
						bcDTO.setToDate(sdf.format(dc.getDate()));
						bcDTO.setBookedBy(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setEmailAddress(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setBookingStatus(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setReqSummary(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setNotifies(bookingSheet.getCell(j++, counter).getContents());
						bcDTO.setBusiness(bookingSheet.getCell(j++, counter).getContents());
						j=0; //Reset Countor
						bookingMap.get(env).add(bcDTO);
					}
				}
				return bookingMap;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(bookingWb);
			}
		}
		return null;
	}

	public boolean addToSchedules(BookConfigDTO bookConfigDTO) 
	{
		Workbook schedulesWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		int nextRow;
		int j=0;
		synchronized (configBookingFile) 
		{
			try 
			{
				WritableCellFormat dateFormat = new WritableCellFormat(	new jxl.write.DateFormat("dd-MM-yyyy"));
				schedulesWB = Workbook.getWorkbook(configBookingFile);
				copy = Workbook.createWorkbook(configBookingFile, schedulesWB);
				sheet = copy.getSheet(0);
				nextRow = sheet.getRows();
				Logger.log(this.getClass().getName(), Logger.INFO, "Record to be added after row : "+ nextRow);
				sheet.addCell(new Label(j++, nextRow, bookConfigDTO.getEnvName()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getProjectName()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getBaContact()));
				sheet.addCell(new Label(j++, nextRow, bookConfigDTO.getManager()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getDevContact()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getQaContact()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getStakeholder()));
				sheet.addCell(new DateTime(j++, nextRow,bookConfigDTO.getFromDateInDF(), dateFormat));
				sheet.addCell(new DateTime(j++, nextRow,bookConfigDTO.getToDateInDF(), dateFormat));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getBookedBy()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getEmailAddress()));
				sheet.addCell( new Label(j++, nextRow, STATUS_APPROVED));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getReqSummary()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getNotifies()));
				sheet.addCell(new Label(j++, nextRow,bookConfigDTO.getBusiness()));
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

	public ArrayList<BookConfigDTO> getConfigsExpiring() 
	{
		Workbook bookingWb = null;
		Sheet bookingSheet = null;
		DateCell fdc = null;
		DateCell tdc = null;
		BookConfigDTO bcDTO = null;
		ArrayList<BookConfigDTO> bcDTOList = null;
		synchronized (configBookingFile) {
			try {
				bookingWb = Workbook.getWorkbook(configBookingFile);
				bookingSheet = bookingWb.getSheet(0);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				bcDTOList = new ArrayList<BookConfigDTO>();
				if (bookingSheet != null) 
				{
					for (int counter = 1; counter < bookingSheet.getRows(); counter++) 
					{
						fdc = (DateCell) bookingSheet.getCell(7, counter);
						tdc = (DateCell) bookingSheet.getCell(8, counter);
						Long diff = (tdc.getDate().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
						Logger.log(this.getClass().getName(), Logger.INFO, " ENV NAME : "+ bookingSheet.getCell(0, counter).getContents() + ", DATE DIFF : "+ diff);
						if (diff <= 7 && diff >= 0) 
						{
							bcDTO = new BookConfigDTO();
							String envName = bookingSheet.getCell(0, counter).getContents();	
							String region = envName.split(":")[0];
							bcDTO.setEnvName(envName);
							bcDTO.setRegion(region);
							bcDTO.setProjectName(bookingSheet.getCell(1,counter).getContents());
							bcDTO.setBaContact(bookingSheet.getCell(2, counter).getContents());
							bcDTO.setManager(bookingSheet.getCell(3, counter).getContents());
							bcDTO.setDevContact(bookingSheet.getCell(4, counter).getContents());
							bcDTO.setQaContact(bookingSheet.getCell(5, counter).getContents());
							bcDTO.setStakeholder(bookingSheet.getCell(6,counter).getContents());
							bcDTO.setFromDate(sdf.format(fdc.getDate()));
							bcDTO.setToDate(sdf.format(tdc.getDate()));
							bcDTO.setBookedBy(bookingSheet.getCell(9, counter).getContents());
							bcDTO.setEmailAddress(bookingSheet.getCell(10,counter).getContents());
							bcDTO.setBookingStatus(bookingSheet.getCell(11,counter).getContents());
							bcDTO.setReqSummary(bookingSheet.getCell(12,counter).getContents());
							bcDTO.setNotifies(bookingSheet.getCell(13,counter).getContents());
							bcDTO.setBusiness(bookingSheet.getCell(14,counter).getContents());
							bcDTOList.add(bcDTO);
						}
					}
				}
				return bcDTOList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(bookingWb);
			}
		}
		return null;
	}
	
	public boolean updateBooking(BookConfigDTO bcDTO) 
	{
		Workbook schedulesWB = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		int j=0;
		synchronized (configBookingFile) 
		{
			try 
			{
				WritableCellFormat dateFormat = new WritableCellFormat(new jxl.write.DateFormat("dd-MM-yyyy"));
				schedulesWB = Workbook.getWorkbook(configBookingFile);
				copy = Workbook.createWorkbook(configBookingFile, schedulesWB);
				sheet = copy.getSheet(0);
				int counter = bcDTO.getLineNo();
				/**
				 * Set Line Number of the request
				 **/
				bcDTO.setLineNo(counter);
				sheet.addCell(new Label(j++, counter, bcDTO.getEnvName()));
				sheet.addCell(new Label(j++, counter, bcDTO.getProjectName()));
				sheet.addCell(new Label(j++, counter, bcDTO.getBaContact()));
				sheet.addCell(new Label(j++, counter, bcDTO.getManager()));
				sheet.addCell(new Label(j++, counter, bcDTO.getDevContact()));
				sheet.addCell(new Label(j++, counter, bcDTO.getQaContact()));
				sheet.addCell(new Label(j++, counter, bcDTO.getStakeholder()));
				sheet.addCell(new DateTime(j++, counter,bcDTO.getFromDateInDF(), dateFormat));
				sheet.addCell(new DateTime(j++, counter, bcDTO.getToDateInDF(),dateFormat));
				sheet.addCell(new Label(j++, counter, bcDTO.getBookedBy()));
				sheet.addCell(new Label(j++, counter, bcDTO.getEmailAddress()));
				/************ Left For Booking Status ************/
				sheet.addCell(new Label(++j, counter, bcDTO.getReqSummary()));
				sheet.addCell(new Label(++j, counter, bcDTO.getNotifies()));
				sheet.addCell(new Label(++j, counter, bcDTO.getBusiness()));
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

}
