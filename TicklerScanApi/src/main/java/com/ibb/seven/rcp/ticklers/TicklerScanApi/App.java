package com.ibb.seven.rcp.ticklers.ticklerscanapi;

import java.util.Scanner;

import org.joda.time.DateTime;

public class App 
{
    public static void main( String[] args )
    {
        CheckTicklerApi check = new CheckTicklerApi();
        String tempStr = check.check("asdasdasd **15m ", null);
        String tempDateStr = check.check("asdasdasd **15m ", tempStr);
        if(tempDateStr != null)
        {
        	System.out.println("TempDateStr: " + tempDateStr);
        	DateTime date = check.toDate(tempDateStr);
        	System.out.println(date);
        }
        else
        {
        	System.out.println("Date not found");
        }
        
//        
//        TicklerScanApi ticklerscan = new TicklerScanApi();
//        DateTime A = (ticklerscan.getTicklerDate("asda **2w"));
//        System.out.println(A);
        Scanner sc = new Scanner(System.in);
        
        sc.nextLine();
    }
}
