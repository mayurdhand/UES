package com.db.riskit.utils.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Logger extends Log4jImpl {
	   
    Logger(Class<Logger> arg0) {
		super(arg0);
	}

	private static Logger _instance;
    
	private static Logger getInstance() {
    	if(_instance == null) {
    		_instance = new Logger(Logger.class);
    	}
    	return _instance;
    }
	
	public static String getStackTrace(Throwable aThrowable) {
	    Writer result = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(result);
	    aThrowable.printStackTrace(printWriter);
	    return result.toString();
	  }
    
	public static void log(String className, int level, String message) {
		getInstance().getLogger().log(debugLevels[level], "[" + className + "] "+": "+ message);		
	}
	
	public static void main(String[] args) {
		System.out.println(">>"+(Long.MAX_VALUE));
	}
}
