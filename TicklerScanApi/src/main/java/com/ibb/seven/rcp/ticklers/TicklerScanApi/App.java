package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import java.util.Scanner;

import org.joda.time.DateTime;

public class App 
{
    public static void main( String[] args )
    {
        
        TicklerScanApi ticklerscan = new TicklerScanApi();
        DateTime A = (ticklerscan.getTicklerDate("asda **33-33-2014"));
        System.out.println(A);
        Scanner sc = new Scanner(System.in);
        
        sc.nextLine();
    }
}
