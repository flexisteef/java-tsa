package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ch.qos.logback.classic.LoggerContext;



/**
 * @author IBB
 * 
 */

public class TicklerScanApi
	{
		private String	RegexTickler		= "\\*\\*(([0-99])+([dD]|[wW]|[mM]|[yY])(([0-99]){1,}([wW]|[mM]|[yY]))?(([0-99]){1,}([mM]|[yY]))?(([0-99]){1,}[yY])?)";
		private String	RegexAbsoluteDate	= "(\\*\\*[0-99]{1,}([-]|[\\/])[0-99]{1,}([-]|[\\/])\\d{4})";
		private String	ticklerStr, absoluteDateStr, currentDate, country = null;

		private int		days, weeks, months, years = 0;
		private int		aDays, aMonths, aYears = 0;
		private int		finalDays, finalMonths, finalYears;

		private DateTime	newDate;
		private String logStr = "com.ibb.seven.rcp.ticklers.TicklerScanApi.";
		private Logger logger = (Logger) LoggerFactory.getLogger(logStr) ;

		public DateTime getTicklerDate(String Note)
			{
		
				
				changeLogStr("getTicklerDate");
				int checkScan = scanNote(Note);
				if (checkScan != 0)
					{
						if (ticklerStr != null)
							{
								setTicklerStr(adjust(getTicklerStr()));
							}
						else
							{
								setAbsoluteDateStr(adjust(getAbsoluteDateStr()));
							}
						logger.debug(getTicklerStr());
						
						
						findLocalDate();
						currentDate();
						splitTickler();
						calculateDate();
						
						
//						 LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//						 StatusPrinter.print(lc);
						LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
						loggerContext.stop();
						return getNewDate();
					}
				else
					{
						logger.warn("No tickler and absolute date found.");
						return null;
					}
			}

		/*
		 * 
		 * ScanNote: input regex, note return string tickler
		 */
		private int scanNote(String Note)
			{
				changeLogStr("scanNote");
				String result = null;
				String currentLine = null;
				String[] regexArray =
					{ RegexTickler, RegexAbsoluteDate };

				for (int i = 0; i < regexArray.length; i++)
					{
						Scanner sc = new Scanner(Note);	// scan string note.
						while (sc.hasNext())								// loop until end of file
							{
								/*
								 * check if currentline meets the requirements for the
								 * regular expression
								 */
								currentLine = sc.next();
								if (currentLine.matches(regexArray[i]))
									{
										result = currentLine;
									}
							}
						sc.close();
						if (result != null)
							{
								if (i == 0)
									{
										setTicklerStr(result);
										logger.debug(getTicklerStr());
									}
								else
									{
										setAbsoluteDateStr(result);
										logger.debug(getAbsoluteDateStr());
										
									}
								return 1;
							}
					}
				return 0;
			}

		/*
		 * 
		 * Removes the ** from the date string
		 */
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

		/*
		 * Split String date into separate int. setDays, set Months set AbsoluteDays, set
		 * AbsoluteMonths. ect.
		 */

		private void splitTickler()
			{
				changeLogStr("splitTickler");
				String tempStr = "0";
				int tempInt = 0;
				int j = 0;
				int[] dateArray;
				String dateStr = (getTicklerStr() != null) ? getTicklerStr() : getAbsoluteDateStr();
				char[] splitDate = new char[dateStr.length()];
				splitDate = dateStr.toCharArray();
				logger.debug("size of array:  " + dateStr.length());
				if (getTicklerStr() != null)
					{
						dateArray = new int[(getTicklerStr().length() - (getTicklerStr().length() / 2))];

						logger.debug("size of new array:" + dateArray.length);
						logger.info("tickler adds: ");
						// --------
						for (int i = 0; i < dateStr.length(); i++)
						// first position of array is 'days' second is 'weeks' ect.
							{
								switch (splitDate[i])
									{
									case 'd':
									case 'D':
										setDays(convertSplitDate(tempStr, " day(s)"));
										j++;
										tempStr = "0";
										break;
									case 'w':
									case 'W':
										setWeeks(convertSplitDate(tempStr, " week(s)"));
										j++;
										tempStr = "0";
										break;
									case 'm':
									case 'M':
										setMonths(convertSplitDate(tempStr, " month(s)"));
										j++;
										tempStr = "0";
										break;
									case 'y':
									case 'Y':
										setYears(convertSplitDate(tempStr, " year(s)"));
										j++;
										tempStr = "0";
										break;
									default:
										tempStr += splitDate[i];
									}
							}
						System.out.println();
					}
				
//					{
//						splitDate = dateStr.toCharArray();
//						dateArray = new int[getAbsoluteDateStr().length()];
//						for (int i = 0; i < getAbsoluteDateStr().length(); i++)
//							{
//								switch (splitDate[i])
//									{
//									case '/':
//									case '-':
//										tempInt = Integer.parseInt(tempStr);
//										dateArray[j] = tempInt;
//										switch (j)
//											{
//											case 0:
//												if ((getCountry() == "nl") | (getCountry() == "NL"))
//													{
//														setADays(dateArray[j]);
//													}
//												else
//													{
//														setAMonths(dateArray[j]);
//													}
//												j++;
//												tempStr = "0";
//												break;
//											case 1:
//												if ((getCountry() == "nl") | (getCountry() == "NL"))
//													{
//														setAMonths(dateArray[j]);
//													}
//												else
//													{
//														setADays(dateArray[j]);
//													}
//												j++;
//												tempStr = "0";
//												break;
//											}
//										break;
//									default:
//										tempStr += splitDate[i];
//									}
//							}
//						tempInt = Integer.parseInt(tempStr);
//						dateArray[j] = tempInt;
//						setAYears(dateArray[j]);
//					}
			}
		private void calculateDate()
			{
				calculateTickler();
				calculateDifference();
			}

		private void calculateTickler()
			{
				changeLogStr("calculateTickler");
				if (getTicklerStr() != null)
					{
						setFinalDays(getDays());
						if (getWeeks() >= 4)
							{
								if ((getWeeks() % 4) == 0) //
									{
										setFinalMonths(getFinalMonths() + (getWeeks() / 4));

									}
								else
									{
										/* remaining weeks will be changed to days */
										setFinalMonths(getFinalMonths() + (getWeeks() / 4));
										setWeeks((getWeeks() % 4));
										setFinalDays((getDays() + (getWeeks() * 7)));
									}
							}
						else
						{
							setFinalDays(getDays() + (getWeeks()*7)); 
						}
						setFinalMonths(getFinalMonths() + getMonths());
						/*
						 * if months is 12 or above, the months will be translated to
						 * years and remaing
						 */
						if (getFinalMonths() >= 12)

							{
								if ((getFinalMonths() % 12) == 0)
									{
										setFinalYears(getYears() + (getFinalMonths() / 12));
										setFinalMonths(0);
									}
//								setFinalYears(getYears() + ((getFinalMonths() / 12)));
								setFinalMonths((getFinalMonths() % 12));
							}
						else
							setFinalYears(getYears() + getFinalYears());
					}
			}

		private void calculateDifference()
			{
				changeLogStr("calculateDifference");
				final String formatStr = (getCountry() == "US") ? "MM-dd-yyyy" : "dd-MM-yyyy";
				DateTimeFormatter dfm = DateTimeFormat.forPattern(formatStr);
				dfm = (getCountry() == "US") ? dfm.withLocale(Locale.US) : dfm.withLocale(Locale.GERMAN);
				final DateTime currentDateTime = dfm.withOffsetParsed().parseDateTime(getCurrentDate());

				if (getTicklerStr() != null)
					{
						DateTime ticklerDateTime = dfm.withOffsetParsed().parseDateTime(getCurrentDate());
						logger.debug("--------------------------");
						logger.debug("days that will be added: \t" + getFinalDays());

						ticklerDateTime = ticklerDateTime.plusDays(getFinalDays());
						logger.debug("months that will be added: \t    " + getFinalMonths());

						ticklerDateTime = ticklerDateTime.plusMonths(getFinalMonths());
						logger.debug("years that will be added: \t\t" + getFinalYears());

						ticklerDateTime = ticklerDateTime.plusYears(getFinalYears());
						String ticklerDate = dfm.print(ticklerDateTime);
						String currentDate = dfm.print(currentDateTime);

						logger.debug("currentdate: \t\t" + currentDate);
						logger.debug("--------------------------" + "\t-----------+");
						logger.info("tickler final date in String: " + ticklerDate);
						logger.info("tickler final date in DateTime : " + ticklerDateTime);
						setNewDate(ticklerDateTime);
					}
				else
					{
						/*set absolutedate to DateTime, replace / with - if date is in US format*/
						String tempStr = getAbsoluteDateStr().replaceAll("/", "-");
						
					
								try
									{
										DateTime absoluteDateTime = dfm.parseDateTime(tempStr);
										logger.info("absolute date: " + absoluteDateTime);
										setNewDate(absoluteDateTime);
									}
								catch (IllegalArgumentException e)
									{
										logger.error("invalid absolute date.");
									}
						
								try
									{
										//absoluteDateTime = new DateTime(getAYears(), getAMonths(), getADays(), 0, 0);
										DateTime absoluteDateTime = dfm.parseDateTime(tempStr);
										logger.info("absolute date: " + absoluteDateTime);
										setNewDate(absoluteDateTime);
									}
								catch (IllegalArgumentException e)
									{
										logger.error("invalid absolute date.");
									}
							
					}
			}
		/*
		 * Find what country / date format the OS uses. Country will be US or NL.
		 */
		private void findLocalDate()
			{
				changeLogStr("findLocalDate");
				System.getProperty("sun.locale.formatasdefault", "true");
				logger.debug("sun.locale.formatasdefault in debug configuration: "
						+ System.getProperty("sun.locale.formatasdefault", "true"));
				String locale = Locale.getDefault().getCountry();// get local. US or NL
				logger.info("date format set for: " + locale);
				setCountry(locale);
			}

		/*
		 * Get current date. setCurrentDate() is output. default is NL format
		 */

		private void currentDate()  // method to get current date
			{
				changeLogStr("currentDate");
				String dateFormat = (getCountry() == "US") ? "MM-dd-yyyy" : "dd-MM-yyyy";
				setCurrentDate(new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime()));
				logger.info("current time: {}", getCurrentDate());
			}

		/*
		 * Input is date in Str. Adds zero's for example: 06-02-2014 Output is date in in.
		 */
		private int convertSplitDate(String converter, String date)
			{
				changeLogStr("convertSplitDate");
				int tempInt = 0;
				tempInt = Integer.parseInt(converter);
				logger.info(tempInt + date + " ");
				return tempInt;
			}
		private void changeLogStr(String add)
			{
				setLogStr("com.ibb.seven.rcp.ticklers.TicklerScanApi.");
				setLogStr(getLogStr() + add);
				
				setLogger((Logger) LoggerFactory.getLogger(getLogStr()));	
				logger.debug("LogStr is:" + getLogStr());
			}

		/*
		 * getters and setters
		 */
		private String getTicklerStr()
			{
				return ticklerStr;
			}

		private void setTicklerStr(String ticklerstr)
			{
				ticklerStr = ticklerstr;
			}

		private String getAbsoluteDateStr()
			{
				return absoluteDateStr;
			}

		private void setAbsoluteDateStr(String absolutedateStr)
			{
				absoluteDateStr = absolutedateStr;
			}

		private String getCountry()
			{
				return country;
			}

		private void setCountry(String country)
			{
				this.country = country;
			}

		private String getCurrentDate()
			{
				return currentDate;
			}

		private void setCurrentDate(String currentDate)
			{
				this.currentDate = currentDate;
			}

		private int getDays()
			{
				return days;
			}

		private void setDays(int days)
			{
				this.days = days;
			}

		private int getWeeks()
			{
				return weeks;
			}

		private void setWeeks(int weeks)
			{
				this.weeks = weeks;
			}

		private int getMonths()
			{
				return months;
			}

		private void setMonths(int months)
			{
				this.months = months;
			}

		private int getYears()
			{
				return years;
			}

		private void setYears(int years)
			{
				this.years = years;
			}

//		private int getADays()
//			{
//				return aDays;
//			}

//		private void setADays(int adays)
//			{
//				aDays = adays;
//			}
//
//		private int getAMonths()
//			{
//				return aMonths;
//			}
//
//		private void setAMonths(int amonths)
//			{
//				aMonths = amonths;
//			}
//
//		private int getAYears()
//			{
//				return aYears;
//			}
//
//		private void setAYears(int ayears)
//			{
//				aYears = ayears;
//			}

		private int getFinalDays()
			{
				return finalDays;
			}

		private void setFinalDays(int finalDays)
			{
				this.finalDays = finalDays;
			}

		private int getFinalMonths()
			{
				return finalMonths;
			}

		private void setFinalMonths(int finalMonths)
			{
				this.finalMonths = finalMonths;
			}

		private int getFinalYears()
			{
				return finalYears;
			}

		private void setFinalYears(int finalYears)
			{
				this.finalYears = finalYears;
			}

		private void setNewDate(DateTime ticklerDateTime)
			{
				this.newDate = ticklerDateTime;
			}

		private DateTime getNewDate()
			{
				return newDate;
			}
		public String getLogStr()
			{
				return logStr;
			}
		public void setLogStr(String logStr)
			{
				this.logStr = logStr;
			}
		public Logger getLogger()
			{
				return logger;
			}
		public void setLogger(Logger logger)
			{
				this.logger = (logger);
			}
	}
