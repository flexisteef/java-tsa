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

/**
 * @author IBB
 * 
 */

public class TicklerScanApi
	{
		private String	RegexTickler		= "\\*\\*(([0-99])+([dD]|[wW]|[mM]|[yY])(([0-99]){1,}([wW]|[mM]|[yY]))?(([0-99]){1,}([mM]|[yY]))?(([0-99]){1,}[yY])?)";
		private String	RegexAbsoluteDate	= "(\\*\\*[0-99]{1,}([-]|[\\/])[0-99]{1,}([-]|[\\/])\\d{4})";
		private String	TicklerStr, AbsoluteDateStr, currentDate, country = null;

		private int		days, weeks, months, years = 0;
		private int		ADays, AMonths, AYears = 0;
		private int		finalDays, finalMonths, finalYears;

		private DateTime	newDate;

		public DateTime getTicklerDate(String Note)
			{
				int checkScan = ScanNote(Note);
				if (checkScan != 0)
					{
						if (TicklerStr != null)
							{
								setTicklerStr(adjust(getTicklerStr()));
							}
						else
							{
								setAbsoluteDateStr(adjust(getAbsoluteDateStr()));
							}
						System.out.println(getTicklerStr());
						findLocalDate();
						currentDate();
						splitTickler();
						CalculateDate();
						return getNewDate();
					}
				else
					{
						System.out.println("No tickler and absolute date found.");
						return null;
					}
			}

		/*
		 * 
		 * ScanNote: input regex, note return string tickler
		 */
		private int ScanNote(String Note)
			{
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
										System.out.println(getTicklerStr());
									}
								else
									{
										setAbsoluteDateStr(result);
										System.out.println(getAbsoluteDateStr());
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
		private static String adjust(String dateStr) 	// dateStr = tickler or absolute
			{
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
						System.out.print(e);
					}
				return newData;
			}

		/*
		 * Split String date into separate int. setDays, set Months set AbsoluteDays, set
		 * AbsoluteMonths. ect.
		 */

		private void splitTickler()
			{
				String tempStr = "0";
				int tempInt = 0;
				int j = 0;
				int[] dateArray;
				String dateStr = (getTicklerStr() != null) ? getTicklerStr() : getAbsoluteDateStr();
				char[] splitDate = new char[dateStr.length()];
				splitDate = dateStr.toCharArray();
				System.out.println("size of array:  " + dateStr.length());
				if (getTicklerStr() != null)
					{
						dateArray = new int[(getTicklerStr().length() - (getTicklerStr().length() / 2))];

						System.out.println("size of new array: " + dateArray.length);
						System.out.print("tickler adds: ");
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
				else
					{
						splitDate = dateStr.toCharArray();
						dateArray = new int[getAbsoluteDateStr().length()];
						for (int i = 0; i < getAbsoluteDateStr().length(); i++)
							{
								switch (splitDate[i])
									{
									case '/':
									case '-':
										tempInt = Integer.parseInt(tempStr);
										dateArray[j] = tempInt;
										switch (j)
											{
											case 0:
												if ((getCountry() == "nl") | (getCountry() == "NL"))
													{
														setADays(dateArray[j]);
													}
												else
													{
														setAMonths(dateArray[j]);
													}
												j++;
												tempStr = "0";
												break;
											case 1:
												if ((getCountry() == "nl") | (getCountry() == "NL"))
													{
														setAMonths(dateArray[j]);
													}
												else
													{
														setADays(dateArray[j]);
													}
												j++;
												tempStr = "0";
												break;
											}
										break;
									default:
										tempStr += splitDate[i];
									}
							}
						tempInt = Integer.parseInt(tempStr);
						dateArray[j] = tempInt;
						setAYears(dateArray[j]);
					}
			}

		private void CalculateDate()
			{
				calculateTickler();
				calculateDifference();
			}

		private void calculateTickler()
			{

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
								setFinalYears(getYears() + ((getFinalMonths() / 12)));
								setFinalMonths((getFinalMonths() % 12));
							}
						else
							setFinalYears(getYears() + getFinalYears());
					}

			}

		private void calculateDifference()
			{

				final String formatStr = (getCountry() == "US") ? "MM-dd-yyyy" : "dd-MM-yyyy";
				DateTimeFormatter dfm = DateTimeFormat.forPattern(formatStr);
				dfm = (getCountry() == "US") ? dfm.withLocale(Locale.US) : dfm.withLocale(Locale.GERMAN);
				final DateTime currentDateTime = dfm.withOffsetParsed().parseDateTime(getCurrentDate());

				if (getTicklerStr() != null)
					{
						DateTime ticklerDateTime = dfm.withOffsetParsed().parseDateTime(getCurrentDate());
						System.out.println("--------------------------");
						System.out.println("days that will be added: \t\t\t" + getFinalDays());

						ticklerDateTime = ticklerDateTime.plusDays(getFinalDays());
						System.out.println("months that will be added: \t\t\t    " + getFinalMonths());

						ticklerDateTime = ticklerDateTime.plusMonths(getFinalMonths());
						System.out.println("years that will be added: \t\t\t\t" + getFinalYears());

						ticklerDateTime = ticklerDateTime.plusYears(getFinalYears());
						String ticklerDate = dfm.print(ticklerDateTime);
						String currentDate = dfm.print(currentDateTime);

						System.out.println("currentdate: \t\t\t\t\t" + currentDate);
						System.out.println("--------------------------" + "\t\t\t-----------+");
						System.out.println("tickler final date in String: \t\t\t" + ticklerDate);
						System.out.println("tickler final date in DateTime : \t\t" + ticklerDateTime);
						setNewDate(ticklerDateTime);
					}
				else
					{
						String tempStr = getAbsoluteDateStr().replaceAll("/", "-");
						
						if (getCountry() == "US")
							{
								try
									{
										DateTime absoluteDateTime = dfm.parseDateTime(tempStr);
										System.out.println("absolute date: " + absoluteDateTime);
										setNewDate(absoluteDateTime);
									}
								catch (IllegalArgumentException e)
									{
										System.out.println("invalid absolute date.");
									}
							}
						else
							{
								try
									{
										//absoluteDateTime = new DateTime(getAYears(), getAMonths(), getADays(), 0, 0);
										
										DateTime absoluteDateTime = dfm.parseDateTime(tempStr);
										System.out.println("absolute date: " + absoluteDateTime);
										setNewDate(absoluteDateTime);
									}
								catch (IllegalArgumentException e)
									{
										System.out.println("invalid absolute date.");
									}
							}
					}
			}

		/*
		 * Find what country / date format the OS uses. Country will be US or NL.
		 */
		private void findLocalDate()
			{
				System.out.println("sun.locale.formatasdefault in debug configuration: "
						+ System.getProperty("sun.locale.formatasdefault"));
				String locale = Locale.getDefault().getCountry();// get local. US or NL
				System.out.println("date format set for: " + locale);
				setCountry(locale);
			}

		/*
		 * Get current date. setCurrentDate() is output. default is NL format
		 */

		private void currentDate()  // method to get current date
			{
				String dateFormat = (getCountry() == "US") ? "MM-dd-yyyy" : "dd-MM-yyyy";
				System.out.print("current time: ");
				setCurrentDate(new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime()));
				System.out.println(getCurrentDate());
			}

		/*
		 * Input is date in Str. Adds zero's for example: 06-02-2014 Output is date in in.
		 */
		private int convertSplitDate(String converter, String date)
			{
				int tempInt = 0;
				tempInt = Integer.parseInt(converter);
				System.out.print(tempInt + date + " ");
				return tempInt;
			}

		/*
		 * getters and setters
		 */
		private String getTicklerStr()
			{
				return TicklerStr;
			}

		private void setTicklerStr(String ticklerStr)
			{
				TicklerStr = ticklerStr;
			}

		private String getAbsoluteDateStr()
			{
				return AbsoluteDateStr;
			}

		private void setAbsoluteDateStr(String absoluteDateStr)
			{
				AbsoluteDateStr = absoluteDateStr;
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

		private int getADays()
			{
				return ADays;
			}

		private void setADays(int aDays)
			{
				ADays = aDays;
			}

		private int getAMonths()
			{
				return AMonths;
			}

		private void setAMonths(int aMonths)
			{
				AMonths = aMonths;
			}

		private int getAYears()
			{
				return AYears;
			}

		private void setAYears(int aYears)
			{
				AYears = aYears;
			}

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

	}
