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
	
	

/**
 * Bank sending data to third party
 * Third Party doing throttling for the process
 * after 31 sec
 * compare should result no error msg
 * 
**/
	@Test
	public void testSent30Sec(){
		bank.send();
		thirdParty.throttle();
		try {
			Thread.sleep(31*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(0,monitor.compare().size());
		
		
	}
	
	/**
	 * Bank sending data to third party
	 * Third Party doing throttling for the process
	 * after29 sec
	 * compare should result no error msg
	 * this method should fail as client has not received any data.
	 * 
	**/
		@Test
		public void testSent29Sec(){
			bank.send();
			thirdParty.throttle();
			try {
				Thread.sleep(29*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			assertEquals(0,monitor.compare().size());
			
		}
		

}
