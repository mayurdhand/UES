package com.db.riskit.utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.db.riskit.utils.logging.Logger;
import com.db.riskit.utils.properties.PropertyConfigurationFactory;
import com.sybase.jdbcx.SybConnection;

public class DBUtils {

	static SybConnection conn = null;

	public static SybConnection getConnection(String jdbcUrl, String user, String password) {
		try {
			String dbDriver = PropertyConfigurationFactory.getInstance().getProperty("DB_DRIVER");
			Class.forName(dbDriver).newInstance();			
			return (SybConnection) DriverManager.getConnection(jdbcUrl+ "?user=" + user.trim() + "&password=" + password.trim());			
		} catch (Exception e) {
			Logger.log(DBUtils.class.getName(), Logger.ERROR, Logger.getStackTrace(e));
		}
		return null;
	}
	
	public static void close(Object obj) throws Exception {
        if (obj != null) {
            if (obj instanceof SybConnection) {
                ((SybConnection) obj).close();
            } else if (obj instanceof Statement) {
                ((Statement) obj).close();
            } else if (obj instanceof ResultSet) {
                ((ResultSet) obj).close();
            }
            obj = null;
        }
    }


}