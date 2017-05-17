package com.db.riskit.utils;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.db.riskit.dto.CCDTO;
import com.db.riskit.utils.logging.Logger;

public class CCClient {

    @SuppressWarnings("unchecked")
    
	public void reloadCache(CCDTO ccDTO)  {
    	JMXConnector jmxc = null;
    	try{
	    	Logger.log(this.getClass().getName(), Logger.INFO, "Create an RMI connector client and connect it to the RMI connector server");
	    	JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+ccDTO.getCcHost()+":"+ccDTO.getCcPort()+"/jmxrmi");
	    	
	        @SuppressWarnings("rawtypes")
			HashMap environment = new HashMap();
	        String[] credentials = new String[] {"control","obvious"};
	        environment.put(JMXConnector.CREDENTIALS, credentials);
	    	jmxc = JMXConnectorFactory.connect(url, environment);
	
	        Logger.log(this.getClass().getName(), Logger.INFO, "Get an MBeanServerConnection");
	        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
	
	        Logger.log(this.getClass().getName(), Logger.INFO, "Domains:");
	        String domains[] = mbsc.getDomains();
	        Arrays.sort(domains);
	        for (String domain : domains) {
	        	Logger.log(this.getClass().getName(), Logger.INFO, "Domain = " + domain);
	        }
	
	        Logger.log(this.getClass().getName(), Logger.INFO, "MBeanServer default domain = " + mbsc.getDefaultDomain());
	        Logger.log(this.getClass().getName(), Logger.INFO, "MBean count = " + mbsc.getMBeanCount());
	
	        Logger.log(this.getClass().getName(), Logger.INFO, "Query MBeanServer MBeans:");
	        Set<ObjectName> names =  new TreeSet<ObjectName>(mbsc.queryNames(null, null));
	        for (ObjectName name : names) {
	        	Logger.log(this.getClass().getName(), Logger.INFO, "ObjectName = " + name);
	        }
	
	        Logger.log(this.getClass().getName(), Logger.INFO, ">>> Perform operations on CacheMutator MBean <<<");
	        ObjectName mbeanName = new ObjectName("com.db.gmrisk.cache:type=CacheMutator");
	        CacheMutator mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, CacheMutator.class, true);
	
	        sleep(1000);
	
	        Logger.log(this.getClass().getName(), Logger.INFO, "Invoke reloadConfig(String database, String config) in CacheMutator MBean...");
	        mbeanProxy.reloadConfig(ccDTO.getDatabase(),ccDTO.getConfig());
	        
		    ccDTO.setMessage(ccDTO.getConfig()+" Cache Reloaded Successfully");  
		    
    	}catch (Exception e) {
    		Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
    		ccDTO.setMessage(e.getMessage());
		}finally{
			Logger.log(this.getClass().getName(), Logger.INFO, "Close the connection to the server");
			try {
				if(jmxc != null){
					jmxc.close();
					Logger.log(this.getClass().getName(), Logger.INFO, "Bye! Bye!");
				}
			} catch (IOException e) {
				Logger.log(this.getClass().getName(), Logger.ERROR, "Error closing server connection");
				Logger.log(this.getClass().getName(), Logger.ERROR, Logger.getStackTrace(e));
			}
		}
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
   
}