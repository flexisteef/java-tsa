package com.ibb.seven.rcp.ticklers.TicklerScanApi;

import java.util.Scanner;

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
        String A = ticklerscan.getTicklerDate("**1d");
        Scanner sc = new Scanner(System.in);
        
        sc.nextLine();
    }
}
