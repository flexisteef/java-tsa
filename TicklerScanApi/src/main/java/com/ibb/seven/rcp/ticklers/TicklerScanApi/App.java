package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import java.util.Scanner;

import org.joda.time.DateTime;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        TicklerScanApi ticklerscan = new TicklerScanApi();
        DateTime A = (ticklerscan.getTicklerDate("asda **2d"));
        System.out.println(A);
        Scanner sc = new Scanner(System.in);
        
        sc.nextLine();
    }
}
