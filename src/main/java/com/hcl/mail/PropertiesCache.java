package com.hcl.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class PropertiesCache {

	final static Logger logger = Logger.getLogger(PropertiesCache.class);
	private final Properties configProp = new Properties();
	private static PropertiesCache INSTANCE = null;
	   private PropertiesCache(String filePath)
	   {
	      //Private constructor to restrict new instances
	      //InputStream in = this.getClass().getClassLoader().getResourceAsStream("email.properties");
		   logger.info(" Reading all properties from file");
		   //InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
		   
		   logger.info(" ******** Done Reading ********* ");
	      try {
	    	  InputStream inputStream = new FileInputStream(new java.io.File(filePath));
	          configProp.load(inputStream);
	      } catch (IOException e) {
	          logger.error("Error while reading properties file "+ e.getMessage());
	      }
	   }
	 
	   
	 
	   public static PropertiesCache getInstance(String filePath)
	   {
	      if(INSTANCE == null)
	      {
	    	  synchronized (PropertiesCache.class) {
				if(INSTANCE == null)
				{
					INSTANCE =  new PropertiesCache(filePath);
				}
			}
	      }
	     
	    	  return INSTANCE;
	   }
	    
	   public String getProperty(String key){
	      return configProp.getProperty(key);
	   }
	    
	   public Set<String> getAllPropertyNames(){
	      return configProp.stringPropertyNames();
	   }
	    
	   public boolean containsKey(String key){
	      return configProp.containsKey(key);
	   }
}
