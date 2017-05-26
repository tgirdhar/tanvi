package com.banking.test;

import com.banking.publish.PricePublishMonitor;

public class PricePublishMonitorTest {
	
	
	/*
	 * This class will start the Price Publish Module after thirty second monitorAndCompare checks if dataSent in last 30 second is same as datareceived by client
	 */
	public static void main(String[] str) throws InterruptedException{
		PricePublishMonitor monitor = new PricePublishMonitor();
		monitor.start();
		Thread.sleep(30*1000);
		System.out.println(monitor.monitorAndCompare());
	}


}
