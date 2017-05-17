package com.db.riskit.utils.logging;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.db.riskit.constants.RiskItPropertiesKeyConstants;


public class Log4jImpl {

	final public static int DEBUG=0;
    final public static int INFO=1;
    final public static int WARNING=2;
    final public static int ERROR=3;
    final public static int FATEL=4;
	
    public static Level[] debugLevels=new Level[]{Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL};
    
	private org.apache.log4j.Logger logger = null;
	
	public Log4jImpl(Class<?> arg0) {
		Properties props = new Properties();		
		try {
			props.load(this.getClass().getResourceAsStream("/properties//log4j.properties"));
			if(!RiskItPropertiesKeyConstants.LOAD_PROPERTIES_FILE_FROM.equalsIgnoreCase("source")){
				props.setProperty("log4j.appender.file.File", RiskItPropertiesKeyConstants.LOG_PATH);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Couldn't load log4j.properties");
		}
		PropertyConfigurator.configure(props);
		logger = org.apache.log4j.Logger.getLogger(arg0);
	}
	
	public Logger getLogger() {
		return logger;
	}
	
}
