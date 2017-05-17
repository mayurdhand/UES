package com.db.riskit.utils.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import com.db.riskit.constants.RiskItPropertiesKeyConstants;
import com.db.riskit.utils.logging.Logger;

public class Properties extends java.util.Properties {
	
	private static final long serialVersionUID = 1L;

	public Properties() throws IllegalArgumentException{
		super();
		initialise();
	}	

	public String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	private void initialise() throws IllegalArgumentException {
		InputStream in;
		String filename;
		try {
			if(RiskItPropertiesKeyConstants.LOAD_PROPERTIES_FILE_FROM.equalsIgnoreCase("source")) {
				filename = RiskItPropertiesKeyConstants.SAP_CONFIGURAION_FILE_NAME;
				in = this.getClass().getResourceAsStream("/properties//" + filename);
				Logger.log(this.getClass().getName(), Logger.INFO, "Loding SAP properties "+filename+" from source");
			} else {
				filename = RiskItPropertiesKeyConstants.SAP_CONFIGURAION_FILE_NAME_WITH_PATH;
				in = new FileInputStream(filename);
				Logger.log(this.getClass().getName(), Logger.INFO, "Loding SAP properties from "+filename);
			}
			load(in);
			in.close();
			if (!validateAppProperties())
				throw new IllegalArgumentException("One or more essential properties not set");
		} catch (IOException e) {
			throw new IllegalArgumentException("Couldn't load properties");
		}
	}

	/**
	 * @return trimmed version of desired property, or null if it doesn't exist
	 */
	public String getProperty(String propertyName) {
		String p = super.getProperty(propertyName);
		return p == null ? null : p;
	}
	
	/**
	 * @return trimmed version of desired property, or null if it doesn't exist
	 */
	public String getUnTrimedProperty(String propertyName) {
		String p = super.getProperty(propertyName);
		return p == null ? null : p;
	}
	
	/**
	 * @return trimmed version of desired property, or null if it doesn't exist
	 */
	public String getProperty(String propertyName, String defaultValue) {
		String p = super.getProperty(propertyName);
		return p == null ? defaultValue : p.trim();
	}

	/**
	 * @return trimmed version of desired property, or null if it doesn't exist
	 */
	public int getIntProperty(String propertyName) {
		String p = super.getProperty(propertyName);
		return p == null ? -1 : Integer.parseInt(p.trim());
	}
	
	/**
	 * @return trimmed version of desired property, or null if it doesn't exist
	 */
	public long getLongProperty(String propertyName) {
		String p = super.getProperty(propertyName);
		return p == null ? -1 : Long.parseLong(p.trim());
	}

	/**
	 * @return trimmed version of desired property, or null if it doesn't exist
	 */
	public boolean getBooleanProperty(String propertyName) {
		String p = super.getProperty(propertyName);
		return p == null ? false : Boolean.parseBoolean(p);
	}
	
	/**
	 * Parse comma separated list of properties from a Properties file. Only
	 * commas may be used for separating items. Not spaces or tabs. Items will
	 * be trimmed before returning them. The following has four items: <br>
	 * "my_prop_array = item1, item item item, item3,item4"
	 * 
	 * @param propertyName
	 *            Name of property which typically represents an array in the
	 *            program
	 * @return array of strings, or null if this property doesn't exist or is an
	 *         empty string
	 */
	public String[] getArrayProperty(String propertyName) {
		String propertyValue = getProperty(propertyName);
		if (propertyValue == null || propertyValue.equals(""))
			return null;
		StringTokenizer st = new StringTokenizer(propertyValue, ",");
		String[] retval = new String[st.countTokens()];
		for (int i = 0; i < retval.length; i++) {
			retval[i] = st.nextToken().trim();
		}
		return retval;
	}

	/**
	 * Application specific subclasses can check that all properties they want
	 * are present and in the correct format. Valid by default.
	 */
	protected boolean validateAppProperties() {
		return true;
	}
}
