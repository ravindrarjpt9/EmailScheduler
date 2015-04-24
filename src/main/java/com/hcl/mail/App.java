package com.hcl.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
	final static Logger logger = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	
    	/*
    	String parameter = "testing";
    	if(logger.isDebugEnabled()){
			logger.debug("This is debug : " + parameter);
		}
 
		if(logger.isInfoEnabled()){
			logger.info("This is info : " + parameter);
		}
 
		logger.warn("This is warn : " + parameter);
		logger.error("This is error : " + parameter);
		logger.fatal("This is fatal : " + parameter);*/
 
    	Calendar c = Calendar.getInstance(); // starts with today's date and time
    	c.add(Calendar.DAY_OF_YEAR, 2);  // advances day by 2
    	Date date = c.getTime(); 
    	System.out.println(date);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String startdate = sdf.format(date);
    	System.out.println(" *** " + startdate + "******");
    	
    	/*Date myDate = new Date();
    	System.out.println(myDate);
    	System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(myDate));
    	System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(myDate));
    	System.out.println(myDate);*/
    }
}
