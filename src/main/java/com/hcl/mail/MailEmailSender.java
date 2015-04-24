package com.hcl.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


public class MailEmailSender {

	final static Logger logger = Logger.getLogger(MailEmailSender.class);
	 
	private static final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-GB; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";
	
	public static void main(String[] args) {
 
		/*for(String s:args)
			logger.info("******** " + s +"***************");
				 System.out.println(PropertiesCache.getInstance().getAllPropertyNames());
		MailEmailSender obj = new MailEmailSender();
		obj.runMe("mkyong");*/
		logger.debug(" Inside mail ");
 
		if(args != null && args.length > 1)
		{
			MailEmailSender obj = new MailEmailSender();
			obj.submit(args[0],args[1]);
		}
		else
		{
			logger.error("Unable to find param attribut :");
		}
 
	}
 
	private void submit(String... arg) {
		
	String soapXml = com.hcl.mail.Util.getSoapXml(arg);	
	if(soapXml != null && soapXml.equals(""))
	{
		String[] soapXmlPackets = Util.getBuiltForSopaXml(arg);
		if(soapXmlPackets != null)
		for(String soaString : soapXmlPackets)
		{
			try {
				logger.info(" SOAP Request : "+soaString);	
				String soapAction ="http://schemas.microsoft.com/exchange/services/2006/messages/CreateItem";
				HttpURLConnection con = connectToEndPoint(PropertiesCache.getInstance(arg[0]).getProperty("WebMailUrl"),PropertiesCache.getInstance(arg[0]).getProperty("Domain"),PropertiesCache.getInstance(arg[0]).getProperty("UserName"),PropertiesCache.getInstance(arg[0]).getProperty("Password"));
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Length", "1000000");
				con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
				con.setRequestProperty("SOAPAction",soapAction);
				con.setRequestProperty("User-Agent", userAgent);
				OutputStream reqStream = con.getOutputStream();
				//System.out.println(spcmsg);
				reqStream.write(soaString.toString().getBytes());
				InputStream resStream = con.getInputStream();
				int ch;
				StringBuffer sb = new StringBuffer();
				while ((ch = resStream.read()) != -1) {
				  sb.append((char) ch);
				}
logger.info("Response from Exchange Server : " +sb);

				if (resStream != null) {
					resStream.close();
				}
			} catch (MalformedURLException e) {
				logger.error("error while sending post request to exchange server :"+e.getMessage());
			} catch (ProtocolException e) {
				logger.error("error while sending post request to exchange server :"+e.getMessage());
				
			} catch (IOException e) {
				logger.error("error while sending post request to exchange server :"+e.getMessage());
			}
		}
		}
	else
	{
		
		try {
			logger.info(" SOAP Request : "+soapXml);	
			String soapAction ="http://schemas.microsoft.com/exchange/services/2006/messages/CreateItem";
			HttpURLConnection con = connectToEndPoint(PropertiesCache.getInstance(arg[0]).getProperty("WebMailUrl"),PropertiesCache.getInstance(arg[0]).getProperty("Domain"),PropertiesCache.getInstance(arg[0]).getProperty("UserName"),PropertiesCache.getInstance(arg[0]).getProperty("Password"));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Length", "1000000");
			con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
			con.setRequestProperty("SOAPAction",soapAction);
			con.setRequestProperty("User-Agent", userAgent);
			OutputStream reqStream = con.getOutputStream();
			//System.out.println(spcmsg);
			reqStream.write(soapXml.toString().getBytes());
			InputStream resStream = con.getInputStream();
			int ch;
			StringBuffer sb = new StringBuffer();
			while ((ch = resStream.read()) != -1) {
			  sb.append((char) ch);
			}
			logger.info("Response from Exchange Server : " +sb);

			if (resStream != null) {
				resStream.close();
			}
		} catch (MalformedURLException e) {
			logger.error("error while sending post request to exchange server :"+e.getMessage());
		} catch (ProtocolException e) {
			logger.error("error while sending post request to exchange server :"+e.getMessage());
		} catch (IOException e) {
			logger.error("error while sending post request to exchange server :"+e.getMessage());
		}
	}
	}

	private static HttpURLConnection connectToEndPoint(String wsEndPoint,String domainName ,String userName,String password) throws MalformedURLException, IOException {
		   
	    
		
	    String webPage = "https://"+wsEndPoint+"/ews/Exchange.asmx";
		//String name = "hcltech\\ravindra_r";
		//String password1 = "Mobiledoc$12";
       userName = domainName+"\\"+userName;
		String authString = userName + ":" + password;
		System.out.println("auth string: " + authString);
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		System.out.println("Base64 encoded auth string: " + authStringEnc);
		URL url = new URL(webPage);
	    URLConnection urlEndPointConnection = url.openConnection();
	    HttpURLConnection httpUrlconnection = (HttpURLConnection) urlEndPointConnection;
	    httpUrlconnection.setDoOutput(true);
	    httpUrlconnection.setDoInput(true);
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(authString.getBytes());
	    httpUrlconnection.setRequestProperty ("Authorization", basicAuth);
	    httpUrlconnection.setRequestMethod("POST");
	    httpUrlconnection.setRequestProperty("content-type", "application/soap+xml;charset=UTF-8");
	    // set connection time out to 2 seconds
	    System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(2 * 1000));
	    // httpUrlconnection.setConnectTimeout(2*1000);
	    // set input stream read timeout to 2 seconds
	    System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(2 * 1000));
	    // httpUrlconnection.setReadTimeout(2*1000);
	    return httpUrlconnection;
	}
	
	/*private void runMe(String parameter){
 
		if(logger.isDebugEnabled()){
			logger.debug("This is debug : " + parameter);
		}
 
		if(logger.isInfoEnabled()){
			logger.info("This is info : " + parameter);
		}
 
		logger.warn("This is warn : " + parameter);
		logger.error("This is error : " + parameter);
		logger.fatal("This is fatal : " + parameter);
 
	}*/
 
}
