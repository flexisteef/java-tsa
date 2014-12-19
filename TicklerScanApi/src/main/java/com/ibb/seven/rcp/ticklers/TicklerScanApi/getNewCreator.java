package com.ibb.seven.rcp.ticklers.ticklerscanapi;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class getNewCreator {

	private String logStr = "com.ibb.seven.rcp.ticklers.TicklerScanApi.";

	private Logger logger = (Logger) LoggerFactory.getLogger(logStr);

	private String regex = "(\\*\\*[a-zA-Z]{3})";
	private String scannedName = null;

	public String getName(String note) {
		scanNote(note);
		setScannedName(adjust(getScannedName()));
		if (getScannedName() != null) {
			return getScannedName();
		} else {
			return null;
		}
	}

	private int scanNote(String note) {
		changeLogStr("scanNote");
		String result = null;
		String currentLine = null;

		Scanner sc = new Scanner(note); // scan string note.
		while (sc.hasNext()) // loop until end of file
		{
			
			/*
			 * check if currentline meets the requirements for the regular
			 * expression
			 */
			currentLine = sc.next();
			if (currentLine.matches(regex)) {
				result = currentLine;
			}
			
		}
		sc.close();
		if (result != null) {
			{
				setScannedName(result);
				logger.debug(getScannedName());
			}
			return 1;
		} else {

			return 0;
		}
	}

	private String adjust(String dateStr) 	// dateStr = tickler or absolute
	{
		changeLogStr("adjust");
		String newData = null;
		String regex = "[^\\*\\*]{2,}"; // removes **
		try
			{
				Pattern pattern = Pattern.compile(regex);    // Regex that removes
															// the **(+) from the
															// name/tickler
				Matcher matcher = pattern.matcher(dateStr);	 // match regex pattern
															// with name/tickler.
				matcher.find();
				newData = matcher.group(0);
			}
		catch (Exception e)
			{
				logger.error(e.toString());
				
			}
		return newData;
	}
	
	private void changeLogStr(String add) {
		setLogStr("com.ibb.seven.rcp.ticklers.TicklerScanApi.");
		setLogStr(getLogStr() + add);

		setLogger((Logger) LoggerFactory.getLogger(getLogStr()));
		logger.debug("LogStr is:" + getLogStr());
	}

	public String getScannedName() {
		return scannedName;
	}

	public void setScannedName(String scannedName) {
		this.scannedName = scannedName;
	}

	public String getLogStr() {
		return logStr;
	}

	public void setLogStr(String logStr) {
		this.logStr = logStr;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
