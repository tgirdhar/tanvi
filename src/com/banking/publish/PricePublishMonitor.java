package com.banking.publish;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.Map.Entry;

import com.banking.Bank;
import com.banking.alert.AlertService;
import com.banking.update.PriceUpdateService;
import com.banking.update.PriceUpdateServiceImplementation;
import com.thirdParty.ThirdParty;

/**
 * 
 * @author tgirdh
 * 
 * PricePublishMonitor start the process.
 * It also provide a method which monitor/compare the value sent to third party
 *
 */

public class PricePublishMonitor {
	
	Bank bank;
	PriceUpdateService service;
	AlertService alertService;
	
	public PricePublishMonitor(){
		service = new PriceUpdateServiceImplementation();
		bank = new Bank(service);
	}
	
	public void start(){
		try{
			bank.send();
			ThirdParty thirdParty = new ThirdParty(service);
			service.subscribeToBankPriceUpdates(thirdParty);
			thirdParty.throttle();
			service.subscribeToCompanyPriceUpdates(bank);
			Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					alertService.alert("Some exception while execution "+e.getMessage());
					
				}
			});
			
		}catch(Exception e){
			e.printStackTrace();
			alertService.alert("Some issue in starting the service");
		}
	}
	
	
	
	public boolean monitorAndCompare(){
		Map<String,Double> clientList = bank.getClientPriceList();
		Map<String,Double> dataSent = bank.getDataSent();
		for(Entry<String,Double> entry : clientList.entrySet()){
			if(dataSent.get(entry.getKey())==null){
				return false;
			}
		}
		return true;
	}
	
	
}
