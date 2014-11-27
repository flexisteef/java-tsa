package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TicklerScanApiTest
	{

				@Test
				public void testGetTicklerDate()
					{
						TicklerScanApi ticklerScanApi = new TicklerScanApi();
						assertEquals(null , ticklerScanApi.getTicklerDate("Test hallo  qwe\n hsdfgsdf sg jjfgh g \n dbcxb *1d *11/7/2014"));
						System.out.println("is het antwoord");
					}

	}
