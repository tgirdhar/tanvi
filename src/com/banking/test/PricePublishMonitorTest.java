/**
 * 
 */
package com.banking.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.banking.Bank;
import com.banking.alert.AlertService;
import com.banking.publish.PricePublishMonitor;
import com.banking.update.PriceUpdateService;
import com.banking.update.PriceUpdateServiceImplementation;
import com.thirdParty.ThirdParty;

/**
 * @author tgirdh
 *
 */
public class PricePublishMonitorTest {
	
	
	PricePublishMonitor monitor;
	Bank bank;
	ThirdParty thirdParty;

	public PricePublishMonitorTest(){
		PriceUpdateService updateService = new PriceUpdateServiceImplementation();
		monitor = new PricePublishMonitor(updateService, new AlertService() {
			
			@Override
			public void alert(String message) {
				System.out.println(message);
				
			}
		});
		bank = new Bank(updateService);
		thirdParty = new ThirdParty(updateService);
	}
	// No Data Sent over bank to ThirdParty and ThirdParty to client
	@Test
	public void testNoDataSent() {
		assertEquals(0,monitor.compare().size());
	}
	
	@Test
	public void test(){
		bank.send();
		thirdParty.throttle();
		try {
			Thread.sleep(40*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0,monitor.compare().size());
		
		
	}

}
