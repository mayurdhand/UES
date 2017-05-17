package com.db.riskit.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.db.riskit.constants.RiskItConstants;
import com.db.riskit.dto.ContactsDTO;
import com.db.riskit.dto.LinksDTO;
import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;

public class CLFileOperations implements RiskItConstants {
	private static final CLFileOperations _instance = new CLFileOperations();
	private File CLFile;

	private CLFileOperations() {
		try {
			CLFile = new File(PropertyConfigurationFactory.getInstance().getProperty(PROPERTY_DATA_PATH)+FILE_CONTACTS_LINKS);
		} catch (Exception e) {
			Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
	}

	public static CLFileOperations getInstance() {
		return _instance;
	}

	private void close(Object wb) {
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

	public List<ContactsDTO> getContactList() {
		Workbook contactWB = null;
		List<ContactsDTO> contactsDTOList = null;
		Sheet contactSheet = null;
		ContactsDTO tempContactsDTO = null;
		synchronized (CLFile) {
			try {
				contactWB = Workbook.getWorkbook(CLFile);
				contactsDTOList = new ArrayList<ContactsDTO>();
				contactSheet = contactWB.getSheet(1);
				for (int counter = 1; counter < contactSheet.getRows(); counter++) {
					String team = contactSheet.getCell(0, counter).getContents();
					if (team != "") {
						tempContactsDTO = new ContactsDTO();
						tempContactsDTO.setTeamId(counter);
						tempContactsDTO.setTeam(team);
						tempContactsDTO.setEmailId(contactSheet.getCell(1,counter).getContents());
						tempContactsDTO.setContactNumber(contactSheet.getCell(2, counter).getContents());
						tempContactsDTO.setPrimaryEscalation(contactSheet.getCell(3, counter).getContents());
						tempContactsDTO.setSecondryEscalation(contactSheet.getCell(4, counter).getContents());
						contactsDTOList.add(tempContactsDTO);
					}
				}
				return contactsDTOList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(contactWB);
			}
		}
		return null;
	}

	public boolean deleteContact(ContactsDTO contactsDTO) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		synchronized (CLFile) {
			try {
				workbook = Workbook.getWorkbook(CLFile);
				copy = Workbook.createWorkbook(CLFile, workbook);
				sheet = copy.getSheet(1);
				sheet.removeRow(contactsDTO.getTeamId());
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
				close(workbook);
			}
		}
		return false;
	}

	public int addContact(ContactsDTO contactsDTO) {
		Workbook contactWB = null;
		WritableWorkbook copy = null;
		WritableSheet conWritable = null;
		synchronized (CLFile) {
			try {
				contactWB = Workbook.getWorkbook(CLFile);
				copy = Workbook.createWorkbook(CLFile, contactWB);
				conWritable = copy.getSheet(1);
				int counter = conWritable.getRows();
				for (int i = 0, j = 1; j < conWritable.getRows(); j++) {
					if ((conWritable.getCell(i, j)).getContents().toString().equals(contactsDTO.getTeam().toString())) {
						return REQUEST_DUPLICATE_ENTRY;
					}
				}				
				Label label = new Label(0, counter,contactsDTO.getTeam());
				conWritable.addCell(label);
				Label label1 = new Label(1, counter,contactsDTO.getEmailId());
				conWritable.addCell(label1);
				Label label2 = new Label(2, counter,contactsDTO.getContactNumber());
				conWritable.addCell(label2);
				Label label3 = new Label(3, counter,contactsDTO.getPrimaryEscalation());
				conWritable.addCell(label3);
				Label label4 = new Label(4, counter,contactsDTO.getSecondryEscalation());
				conWritable.addCell(label4);
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
				close(contactWB);
			}
		}
		return REQUEST_FAILURE;
	}

	public boolean updateContact(ContactsDTO contactsDTO) {
		Workbook contactWB = null;
		WritableWorkbook copy = null;
		WritableSheet conWritable = null;
		synchronized (CLFile) {
			try {
				contactWB = Workbook.getWorkbook(CLFile);
				copy = Workbook.createWorkbook(CLFile, contactWB);
				conWritable = copy.getSheet(1);
				for (int i = 0, j = 1; j < conWritable.getRows(); j++) {
					if ((conWritable.getCell(i, j)).getContents().toString().equals(contactsDTO.getTeam().toString())) {
						Label label = new Label(0, j,contactsDTO.getTeam());
						conWritable.addCell(label);
						Label label1 = new Label(1, j,contactsDTO.getEmailId());
						conWritable.addCell(label1);
						Label label2 = new Label(2, j,contactsDTO.getContactNumber());
						conWritable.addCell(label2);
						Label label3 = new Label(3, j,contactsDTO.getPrimaryEscalation());
						conWritable.addCell(label3);
						Label label4 = new Label(4, j,contactsDTO.getSecondryEscalation());
						conWritable.addCell(label4);
					}
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
				close(contactWB);
			}
		}
		return false;
	}

	public List<LinksDTO> getLinksList() {
		Workbook linksWb = null;
		List<LinksDTO> linksDTOList = null;
		LinksDTO tempLinksDTO = null;
		Sheet linkSheet = null;
		synchronized (CLFile) {
			try {
				linksWb = Workbook.getWorkbook(CLFile);
				linksDTOList = new ArrayList<LinksDTO>();
				linkSheet = linksWb.getSheet(0);
				for (int counter = 1; counter < linkSheet.getRows(); counter++) {
					String application = linkSheet.getCell(0, counter).getContents();
					if (application != "") {
						tempLinksDTO = new LinksDTO();
						tempLinksDTO.setLinkId(counter);
						tempLinksDTO.setApplicationName(application);
						tempLinksDTO.setUrl(linkSheet.getCell(1, counter).getContents());
						tempLinksDTO.setApplicationDesc(linkSheet.getCell(2,counter).getContents());
						linksDTOList.add(tempLinksDTO);
					}
				}
				return linksDTOList;
			} catch (BiffException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} catch (Exception e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			} finally {
				close(linksWb);
			}
		}
		return null;
	}

	public int addLink(LinksDTO linksDTO) {
		Workbook linksWb = null;
		WritableWorkbook copy = null;
		WritableSheet conWritable = null;
		synchronized (CLFile) {
			try {
				linksWb = Workbook.getWorkbook(CLFile);
				copy = Workbook.createWorkbook(CLFile, linksWb);
				conWritable = copy.getSheet(0);
				int counter = conWritable.getRows();
				for (int i = 0, j = 1; j < conWritable.getRows(); j++) {
					if ((conWritable.getCell(i, j)).getContents().toString().equals(linksDTO.getApplicationName().toString())) {
						return REQUEST_DUPLICATE_ENTRY;
					}
				}				
				Label label2 = new Label(0, counter,linksDTO.getApplicationName());
				conWritable.addCell(label2);
				Label label3 = new Label(1, counter,linksDTO.getUrl());
				conWritable.addCell(label3);
				Label label4 = new Label(2, counter,linksDTO.getApplicationDesc());
				conWritable.addCell(label4);
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
				close(linksWb);
			}
		}
		 return REQUEST_FAILURE;
	}

	public boolean deleteLink(LinksDTO linksDTO) {
		Workbook workbook = null;
		WritableWorkbook copy = null;
		WritableSheet sheet = null;
		synchronized (CLFile) {
			try {
				workbook = Workbook.getWorkbook(CLFile);
				copy = Workbook.createWorkbook(CLFile, workbook);
				sheet = copy.getSheet(0);
				sheet.removeRow(linksDTO.getLinkId());
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
				close(workbook);
			}
		}
		return false;
	}
	
	public boolean updateLink(LinksDTO linksDTO) {
		Workbook linksWb = null;
		WritableWorkbook copy = null;
		WritableSheet conWritable = null;
		synchronized (CLFile) {
			try {
				linksWb = Workbook.getWorkbook(CLFile);
				copy = Workbook.createWorkbook(CLFile, linksWb);
				conWritable = copy.getSheet(0);
				for (int i = 0, j = 1; j < conWritable.getRows(); j++) {
					if ((conWritable.getCell(i, j)).getContents().toString().equals(linksDTO.getApplicationName().toString())) {
						Label label = new Label(0, j, linksDTO.getApplicationName());
						conWritable.addCell(label);
						Label label1 = new Label(1, j, linksDTO.getUrl());
						conWritable.addCell(label1);
						Label label2 = new Label(2, j, linksDTO.getApplicationDesc());
						conWritable.addCell(label2);	
					}
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
				close(linksWb);
			}
		}
		return false;
	}


}
