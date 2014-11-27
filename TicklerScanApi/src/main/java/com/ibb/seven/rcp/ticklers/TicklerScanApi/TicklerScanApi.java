package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author IBB
 * 
 */

public class TicklerScanApi
	{
		private String	RegexTickler		= "\\*\\*(([0-99])+([dD]|[wW]|[mM]|[yY])(([0-99]){1,}([wW]|[mM]|[yY]))?(([0-99]){1,}([mM]|[yY]))?(([0-99]){1,}[yY])?)";
		private String	RegexAbsoluteDate	= "(\\*\\*[0-99]{1,}([-]|[\\/])[0-99]{1,}([-]|[\\/])\\d{4})";
		private String	TicklerStr, AbsoluteDateStr, currentDate, country = null;

		int				days, weeks, months, years = 0;
		int				CDays, CMonths, CYears = 0;
		int				ADays, AMonths, AYears = 0;
		int				finalDays, finalMonths, finalYears;
		String 			finalTickler = null;
		
		public String getTicklerDate(String Note)
			{
				int checkScan = ScanNote(Note);
				String dateStr;
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
						return (getTicklerStr() != null ) ? getFinalTickler() : getAbsoluteDateStr().toString();
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
		public int ScanNote(String Note)
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
		public static String adjust(String dateStr) 	// dateStr = tickler or absolute
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

		public void splitTickler()
			{
				String converter = "0";
				int tempInt, j = 0;
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
										tempInt = Integer.parseInt(converter);
										dateArray[j] = tempInt;
										System.out.print(dateArray[j] + " day(s)");
										setDays(dateArray[j]);
										j++;
										converter = "0";
										break;
									case 'w':
									case 'W':
										tempInt = Integer.parseInt(converter);
										dateArray[j] = tempInt;
										System.out.print(" - " + dateArray[j] + " week(s)");
										setWeeks(dateArray[j]);
										j++;
										converter = "0";
										break;
									case 'm':
									case 'M':
										tempInt = Integer.parseInt(converter);
										dateArray[j] = tempInt;
										System.out.print(" - " + dateArray[j] + " month(s)");
										setMonths(dateArray[j]);
										j++;
										converter = "0";
										break;
									case 'y':
									case 'Y':
										tempInt = Integer.parseInt(converter);
										dateArray[j] = tempInt;
										System.out.print(" - " + dateArray[j] + " year(s)");
										setYears(dateArray[j]);
										j++;
										converter = "0";
										break;
									default:
										converter += splitDate[i];
									}
							}
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
										tempInt = Integer.parseInt(converter);
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
												converter = "0";
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
												converter = "0";
												break;
											}
										break;
									default:
										converter += splitDate[i];
									}
							}
						tempInt = Integer.parseInt(converter);
						dateArray[j] = tempInt;
						setAYears(dateArray[j]);
					}
			}

		public void CalculateDate()
			{
				calculateTickler();
				calculateDifference();
			}

		public String calculateTickler()
			{
				String total = "0";
				if(getTicklerStr() != null)
					{
				
				setFinalDays(getDays());
				if (getWeeks() >= 4) // if there are 4 or more weeks. The weeks will be
										// separated into months and days
					{
						if ((getWeeks() % 4) == 0) //
							{
								setFinalMonths(getFinalMonths() + (getWeeks() / 4));
								// all weeks will change to months
							}
						else
							{
								setFinalMonths(getFinalMonths() + (getWeeks() / 4));
								setWeeks((getWeeks() % 4)); // remaining weeks will be
															// changed to days
								setFinalDays((getDays() + (getWeeks() * 7)));
							}
					}
				// else the weeks will be multiplied by 7.
				// this is to get rid of the weeks variable.
				setFinalMonths(getFinalMonths() + getMonths());
				if (getFinalMonths() >= 12) // if months is 12 or above, the months will
											// be translated to years and remaing
				// months
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

				// makes a string date of currentdate. // nothing more.
				total = Integer.toString(getFinalDays());
				total += "-";
				total += Integer.valueOf(getFinalMonths()).toString();
				total += "-";
				total += Integer.valueOf(getFinalYears()).toString();
				System.out.println("\n-------------------------\ntotal days: \t" + getFinalDays());
				System.out.println("total months: \t" + getFinalMonths());
				System.out.println("total years: \t" + getFinalYears() + "\n-------------------------");
				System.out.println("tickler adds: \t" + total + "\n-------------------------");
			
			}
				return total;
			}

		public void calculateDifference()
			{
				String formatStr = (getCountry() == "US") ? "MM/dd/yyyy" : "dd-MM-yyyy";
				// default is NL format
				DateFormat dF1 = new SimpleDateFormat(formatStr); // format for calendar
				dF1.setLenient(false); // makes validation more strict
				Calendar currentCalendar = new GregorianCalendar(); // make object
																	// calendar
				currentCalendar = Calendar.getInstance(); // put current time in calendar
				String ticklerDate = dF1.format(currentCalendar.getTime()); // format time
																			// to nl or us
																			// dateformat
				System.out.println("current date: \t\t\t" + ticklerDate);

				if (getTicklerStr() != null)
					{
						System.out.println("days that will be added: \t" + getFinalDays());
						currentCalendar.add(Calendar.DAY_OF_MONTH, getFinalDays());// adds
																					// ticklerdays

						System.out.println("months that will be added: \t    " + getFinalMonths());
						currentCalendar.add(Calendar.MONTH, getFinalMonths());// adds
																				// ticklermonths

						System.out.println("years that will be added: \t\t" + getFinalYears());
						currentCalendar.add(Calendar.YEAR, getFinalYears()); // adds
																				// tickleryears
						ticklerDate = dF1.format(currentCalendar.getTime());
						// put new date in calStr var
						System.out.println("new date: \t\t\t" + ticklerDate);
						setFinalTickler(ticklerDate);
					}
				else
					{
						//DateFormat dF2 = new SimpleDateFormat(formatStr);
						dF1.setLenient(false); // makes validation more strict
						Calendar absoluteCalendar = new GregorianCalendar();
						// absoluteCalendar = absoluteCalendar.getInstance(); // put
						// current time in calendar
						absoluteCalendar.set(getAYears(), (getAMonths() - 1), getADays());
						String absoluteCalendarStr = dF1.format(absoluteCalendar.getTime());
						System.out.println("absolute date: \t\t\t" + absoluteCalendarStr);
						setAbsoluteDateStr(absoluteCalendarStr);
					}
			}

		/*
		 * Find what country / date format the OS uses. Country will be US or NL.
		 */
		public void findLocalDate()
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

		public void currentDate()  // method to get current date
			{
				String dateFormat = (getCountry() == "US") ? "MM-dd-yyyy" : "dd-MM-yyyy";
				System.out.print("current time: ");
				setCurrentDate(new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime()));
				System.out.println(getCurrentDate());
			}

		/*
		 * getters and setters
		 */
		public String getTicklerStr()
			{
				return TicklerStr;
			}

		public void setTicklerStr(String ticklerStr)
			{
				TicklerStr = ticklerStr;
			}

		public String getAbsoluteDateStr()
			{
				return AbsoluteDateStr;
			}

		public void setAbsoluteDateStr(String absoluteDateStr)
			{
				AbsoluteDateStr = absoluteDateStr;
			}

		public String getCountry()
			{
				return country;
			}

		public void setCountry(String country)
			{
				this.country = country;
			}

		public String getCurrentDate()
			{
				return currentDate;
			}

		public void setCurrentDate(String currentDate)
			{
				this.currentDate = currentDate;
			}

		public int getDays()
			{
				return days;
			}

		public void setDays(int days)
			{
				this.days = days;
			}

		public int getWeeks()
			{
				return weeks;
			}

		public void setWeeks(int weeks)
			{
				this.weeks = weeks;
			}

		public int getMonths()
			{
				return months;
			}

		public void setMonths(int months)
			{
				this.months = months;
			}

		public int getYears()
			{
				return years;
			}

		public void setYears(int years)
			{
				this.years = years;
			}

		public int getCDays()
			{
				return CDays;
			}

		public void setCDays(int cDays)
			{
				CDays = cDays;
			}

		public int getCMonths()
			{
				return CMonths;
			}

		public void setCMonths(int cMonths)
			{
				CMonths = cMonths;
			}

		public int getCYears()
			{
				return CYears;
			}

		public void setCYears(int cYears)
			{
				CYears = cYears;
			}

		public int getADays()
			{
				return ADays;
			}

		public void setADays(int aDays)
			{
				ADays = aDays;
			}

		public int getAMonths()
			{
				return AMonths;
			}

		public void setAMonths(int aMonths)
			{
				AMonths = aMonths;
			}

		public int getAYears()
			{
				return AYears;
			}

		public void setAYears(int aYears)
			{
				AYears = aYears;
			}

		public int getFinalDays()
			{
				return finalDays;
			}

		public void setFinalDays(int finalDays)
			{
				this.finalDays = finalDays;
			}

		public int getFinalMonths()
			{
				return finalMonths;
			}

		public void setFinalMonths(int finalMonths)
			{
				this.finalMonths = finalMonths;
			}

		public int getFinalYears()
			{
				return finalYears;
			}

		public void setFinalYears(int finalYears)
			{
				this.finalYears = finalYears;
			}
		public String getFinalTickler()
			{
				return finalTickler;
			}
		public void setFinalTickler(String finalTickler)
			{
				this.finalTickler = finalTickler;
			}

	}
