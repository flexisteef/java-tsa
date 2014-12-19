package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.ibb.seven.rcp.ticklers.ticklerscanapi.TicklerScanApi;

public class TicklerScanApiTest
{

  TicklerScanApi ticklerScanApi;

  @Before
  public void setup()
  {
    ticklerScanApi = new TicklerScanApi();
  }

  @Test
  public void testGetTicklerDate()
  {
    assertEquals(null, ticklerScanApi.getTicklerDate("Test hallo  qwe\n hsdfgsdf sg jjfgh g \n dbcxb *1d *11/7/2014"));
    System.out.println("is het antwoord");
  }


  @Test
  public void testGetTicklerDateDayString() {
    String noteText = "tekst er voor **1d en nog \n\n wat tekst er achter";
    DateTime expectedDateTime =  new DateTime().plusDays(1).withTimeAtStartOfDay();

    assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));

    noteText = "tekst er voor **3d en nog \n\n wat tekst er achter, maar ook met nog een tickler **2d";
    expectedDateTime =  new DateTime().plusDays(3).withTimeAtStartOfDay();
  }

  @Test
  public void testGetTicklerDateWeekString() {
    String noteText = "tekst er voor **2w en nog \n\n wat tekst er achter";
    DateTime expectedDateTime =  new DateTime().plusDays(14).withTimeAtStartOfDay();

    assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));

    noteText = "tekst er voor **1w en nog \n\n wat tekst er achter, maar ook met nog een tickler **1w";
    expectedDateTime =  new DateTime().plusDays(7).withTimeAtStartOfDay();
    
    assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));
    
    noteText = "tekst er voor **1w en nog \n\n wat tekst er achter, maar ook met nog een tickler **4w";
    expectedDateTime =  new DateTime().plusMonths(1).withTimeAtStartOfDay();
    
    assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));
  }
  @Test
  public void testGetTicklerDateWeekStringFiveWeeks() {
	  String noteText = "tekst er voor **1w en nog \n\n wat tekst er achter, maar ook met nog een tickler **5w";
	  DateTime expectedDateTime =  new DateTime().plusDays(7).withTimeAtStartOfDay();
	    expectedDateTime = expectedDateTime.plusMonths(1).withTimeAtStartOfDay();
	    assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));
  }

  @Test
  public void testGetTicklerDateMonthString() {
	  String noteText = "tekst er voor **12m en nog \n\n wat tekst er achter";
	  DateTime expectedDateTime =  new DateTime().plusYears(1).withTimeAtStartOfDay();
	  
	  assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));

	    noteText = "tekst er voor **1w en nog \n\n wat tekst er achter, maar ook met nog een tickler **5m";
	    expectedDateTime =  new DateTime().plusMonths(5).withTimeAtStartOfDay();
	    
	     noteText = "tekst er voor **13m en nog \n\n wat tekst er achter";
		 expectedDateTime =  new DateTime().plusYears(1).withTimeAtStartOfDay();
		 expectedDateTime = expectedDateTime.plusMonths(1).withTimeAtStartOfDay();
		  assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));
	    
  }


  @Test
  public void testGetTicklerDateYearString() {
	  String noteText = "tekst er voor **12y en nog \n\n wat tekst er achter";
	  DateTime expectedDateTime =  new DateTime().plusYears(12).withTimeAtStartOfDay();
	  
	  assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));

	    noteText = "tekst er voor **5y en nog \n\n wat tekst er achter, maar ook met nog een tickler **0m";
	    expectedDateTime =  new DateTime().plusYears(0).withTimeAtStartOfDay();
  }

  @Test
  public void testGetTicklerDateAbsoluteDateString() {
	  String noteText = "tekst er voor **12-12-2014 en nog \n\n wat tekst er achter";
	  DateTime expectedDateTime =  new DateTime().withDate(2014, 12, 12).withTimeAtStartOfDay();
	  
	  assertEquals(expectedDateTime, ticklerScanApi.getTicklerDate(noteText));

	    noteText = "tekst er voor *5y en nog \n\n wat tekst er achter, maar ook met nog een tickler **1-1-2014";
	    expectedDateTime =  new DateTime().withDate(2014, 1, 1).withTimeAtStartOfDay();
  }
  
  @Test
  public void testCalculateDifferenceInvalidAbsoluteDate(){
	  String noteText = "tekst er voor **33-33-2014 en nog \n\n wat tekst er achter";
	  String expectedFormatStr = null;
	  
	  assertEquals(expectedFormatStr, ticklerScanApi.getTicklerDate(noteText));
	  
	  noteText = "tekst er voor **33-33-2014 en nog \n\n wat tekst er achter **02d";
	  DateTime expectedDate = new DateTime().plusDays(2).withTimeAtStartOfDay();
	  assertEquals(expectedDate, ticklerScanApi.getTicklerDate(noteText));
  }
  @Test
  public void testCalculateTicklerInvalid(){
	  String noteText = "tekst er voor **0ww en nog \n\n wat tekst er achter 1w";
	  String expectedFormatStr = null;
	  
	  assertEquals(expectedFormatStr, ticklerScanApi.getTicklerDate(noteText));
  }
  
}
