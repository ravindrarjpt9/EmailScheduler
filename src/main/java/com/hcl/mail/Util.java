package com.hcl.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class Util {

	final static Logger logger = Logger.getLogger(Util.class);

	private static String spcmsg = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ "\n"
			+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
			+ "\n"
			+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
			+ "\n"
			+ "xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">"
			+ "\n"
			+ "<soap:Body>"
			+ "\n"
			+ "<CreateItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\""
			+ "\n"
			+ "xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\""
			+ "\n"
			+ "SendMeetingInvitations=\"SendToAllAndSaveCopy\" >"
			+ "\n"
			+ "<SavedItemFolderId>"
			+ "\n"
			+ "<t:DistinguishedFolderId Id=\"calendar\"/>"
			+ "\n"
			+ "</SavedItemFolderId>"
			+ "\n"
			+ "<Items>"
			+ "\n"
			+ "<t:CalendarItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/types\">"
			+ "\n"
			+ "<Subject>#Subject#</Subject>"
			+ "\n"
			+ "<Body BodyType=\"Text\">#Body#</Body>"
			+ "\n"
			+ "<ReminderIsSet>#ReminderIsSet#</ReminderIsSet>"
			+ "\n"
			+ "<ReminderMinutesBeforeStart>#ReminderMinutesBeforeStart#</ReminderMinutesBeforeStart>"
			+ "\n"
			+ "<Start>#Start#</Start>"
			+ "\n"
			+ "<End>#End#</End>"
			+ "\n"
			+ "<IsAllDayEvent>#IsAllDayEvent#</IsAllDayEvent>"
			+ "\n"
			+ "<LegacyFreeBusyStatus>#LegacyFreeBusyStatus#</LegacyFreeBusyStatus>"
			+ "\n"
			+ "<Location>#Location#</Location>"
			+ "\n"
			+ "<RequiredAttendees>"
			+ "\n"
			+ "<Attendee>"
			+ "\n"
			+ "<Mailbox>"
			+ "\n"
			+ "<EmailAddress>#Attendee#</EmailAddress>"
			+ "\n"
			+ "</Mailbox>"
			+ "\n"
			+ "</Attendee>"
			+ "\n"
			+"#AttendeeEmails#"
			
			/*
			 * "<Attendee>"+"\n"+ "<Mailbox>"+"\n"+
			 * "<EmailAddress>Ravindra.Rajpoot@contractor.ca.com</EmailAddress>"
			 * +"\n"+ "</Mailbox>"+"\n"+ "</Attendee>"+"\n"+
			 */
			+"</RequiredAttendees>"
			+ "\n"
			+ "</t:CalendarItem>"
			+ "\n"
			+ "</Items>"
			+ "\n"
			+ "</CreateItem>"
			+ "\n"
			+ "</soap:Body>"
			+ "\n" + "</soap:Envelope>";

	private static String sendEmail = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ "\n"
			+ "xmlns:m=\"http://schemas.microsoft.com/exchange/services/2006/messages\""
			+ "\n"
			+ "xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\""
			+ "\n"
			+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
			+ "\n"
			+ "<soap:Header>"
			+ "\n"
			+ "<t:RequestServerVersion Version=\"#Version#\" />"
			+ "\n"
			+ "</soap:Header>"
			+ "\n"
			+ "<soap:Body>"
			+ "\n"
			+ "<m:CreateItem MessageDisposition=\"SendAndSaveCopy\">"
			+ "\n"
			+ "<m:SavedItemFolderId>"
			+ "\n"
			+ "<t:DistinguishedFolderId Id=\"sentitems\" />"
			+ "\n"
			+ "</m:SavedItemFolderId>"
			+ "\n"
			+ "<m:Items>"
			+ "\n"
			+ "<t:Message>"
			+ "\n"
			+ "<t:Subject>#emailSubject#</t:Subject>"
			+ "\n"
			+ "<t:Body BodyType=\"HTML\">#emailBody#</t:Body>"
			+ "\n"
			+ "<t:ToRecipients>"
			+ "\n"
			+ "<t:Mailbox>"
			+ "\n"
			+ "<t:EmailAddress>#emailAddress#</t:EmailAddress>"
			+ "\n"
			+ "</t:Mailbox>"
			+"#toRecipients#"
			+ "\n"
			+ "</t:ToRecipients>"
			+ "\n"
			+"CCemailAddress"
			+ "</t:Message>"
			+ "\n"
			+ "</m:Items>"
			+ "\n"
			+ "</m:CreateItem>"
			+ "\n" + "</soap:Body>" + "\n" + "</soap:Envelope>";

	public static String getSoapXml(String... arg) {
		if ((arg[0] != null && arg[1] != null)
				&& (arg[1].equalsIgnoreCase("calender"))) {
			return getBulidCalenderSoapXml(arg[0]);

		} else if ((arg[0] != null && arg[1] != null)
				&& (arg[1].equalsIgnoreCase("email"))) {
			return getBuildEmailSoabXml(arg[0]);
		}
		if ((arg[0] != null && arg[1] != null)
				&& (arg[1].equalsIgnoreCase("both"))) {
			return "";
		}
		return null;
	}

	private static String getBuildEmailSoabXml(String filePath) {

		PropertiesCache properties = PropertiesCache.getInstance(filePath);
		sendEmail = sendEmail.replace("#Version#", properties.getProperty("Version")).replace("#emailSubject#", properties.getProperty("emailSubject")).replace("#emailBody#",getBuildEmailBody(properties.getProperty("emailBody"),properties.getProperty("emailSignature")));
		if(properties.getProperty("emailAddress").trim().split(";").length==1)
		{
			sendEmail = sendEmail.replace("#emailAddress#", properties.getProperty("emailAddress").trim().split(";")[0]).replace("#toRecipients#", "");
		}
		else
		{
			sendEmail = sendEmail.replace("#emailAddress#", properties.getProperty("emailAddress").trim().split(";")[0]).replace("#toRecipients#", getListOfSenderEmails(properties.getProperty("emailAddress")));
		}
		if((properties.getProperty("CCemailAddress") != null && properties.getProperty("CCemailAddress").length() > 5) && properties.getProperty("CCemailAddress").trim().split(";").length >= 1)
		{
			sendEmail = sendEmail.replace("CCemailAddress", getBuildCCemailAddress(properties.getProperty("CCemailAddress")));
		}
		else
		{
			sendEmail = sendEmail.replace("CCemailAddress", "");
		}
		return sendEmail;
	}

	private static String getBuildCCemailAddress(String cclist) {
		String temp = "<t:Mailbox>"+"\n"+"<t:EmailAddress>#moneyEmails#</t:EmailAddress>"+"\n"+"</t:Mailbox>";

		String resp = "";
		String sender[] = cclist.split(";");
		for(int i =0;i<sender.length;i++) 
		{
			resp+= temp.replace("#moneyEmails#", sender[i].trim());
		}
		return "<t:CcRecipients>"+"\n"+resp+"\n"+"</t:CcRecipients>";
	}

	private static String getBulidCalenderSoapXml(String filePath) {
		PropertiesCache properties = PropertiesCache.getInstance(filePath);
		spcmsg = spcmsg.replace("#Subject#", properties.getProperty("Subject")).replace("#Body#", properties.getProperty("BodyText")).replace("#ReminderIsSet#", properties.getProperty("ReminderIsSet").trim());
		spcmsg = spcmsg.replace("#ReminderMinutesBeforeStart#", properties.getProperty("ReminderMinutesBeforeStart")).replace("#Start#", getDate(properties.getProperty("Start"),Integer.parseInt(properties.getProperty("afterDayScheduler")))).replace("#End#", getDate(properties.getProperty("End"),Integer.parseInt(properties.getProperty("afterDayScheduler"))));
		spcmsg = spcmsg.replace("#IsAllDayEvent#", properties.getProperty("IsAllDayEvent")).replace("#LegacyFreeBusyStatus#", properties.getProperty("LegacyFreeBusyStatus")).replace("#Location#", properties.getProperty("Location"));
		if(properties.getProperty("Attendees").trim().split(";").length==1)
		{
			spcmsg = spcmsg.replace("#Attendee#", properties.getProperty("Attendees").trim().split(";")[0]).replace("#AttendeeEmails#", "");
		}
		else
		{
			spcmsg = spcmsg.replace("#Attendee#", properties.getProperty("Attendees").trim().split(";")[0]).replace("#AttendeeEmails#", getAttendeeEmails(properties.getProperty("Attendees")));
		}
		return spcmsg;
	}

	private static String getBuildEmailBody(String body,String emailSignature)
	{
		String s = body
				+"\n\n"
				+"Thank You,"
				+"\n"
			    +emailSignature;
		return s;
				
	}
	private static String getAttendeeEmails(String senders) {
		String temp = "<Attendee>"+"\n"+ "<Mailbox>"+"\n"+"<EmailAddress>#moneyEmails#</EmailAddress>"+"\n"+ "</Mailbox>"+"\n"+ "</Attendee>"+"\n";
		String sender[] = senders.split(";");
		String resp = "";
		for(int i =1;i<sender.length;i++) 
		{
			resp+= temp.replace("#moneyEmails#", sender[i].trim());
		}
		return resp;
	}
	private static String getListOfSenderEmails(String sendrs)
	{
		String temp = "<t:Mailbox>"+"\n"+"<t:EmailAddress>#moneyEmails#</t:EmailAddress>"+"\n"+"</t:Mailbox>"+"\n";

		String resp = "";
		String sender[] = sendrs.split(";");
		for(int i =1;i<sender.length;i++) 
		{
			resp+= temp.replace("#moneyEmails#", sender[i].trim());
		}
		return resp;
	}
	public static String[] getBuiltForSopaXml(String...arg) {

		return new String[] { getBulidCalenderSoapXml(arg[0]),
				getBuildEmailSoabXml(arg[0]) };
	}
	
	public static String getDate(String time,int day)
	{
		String sdate = "";
		logger.info("Get date for " +time );
		Calendar c = Calendar.getInstance(); // starts with today's date and time
    	c.add(Calendar.DAY_OF_YEAR, day);  // advances day by 2
    	Date date = c.getTime(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	sdate = sdf.format(date).concat("T").concat(time);
    	logger.info("Final Calendrer Date : " + sdate);
    	return sdate;
	}
}
