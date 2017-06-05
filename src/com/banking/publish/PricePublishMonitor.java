package com.banking.publish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.banking.alert.AlertService;
import com.banking.listener.PriceListener;
import com.banking.update.PriceUpdateService;
import com.thirdParty.ThirdPartyMap;

/**
 * 
 * @author tgirdh
 * 
 * PricePublishMonitor monitor the data sent by Third party to Bank.
 * It has two inner claases  as below 
 * -> PricePublisher which subscribe to bank data and saves into map 
 * -> PriceMonitor which subscribe as as client data and saves symbol and price in HashMap
 *  compare method compare data from both the map  and if there is mismatch it will send msg of data mismatch with symbol  
 *  Monitor method start a timer thread which start after 31 sec and then scheduled for 30 sec.
 *  It will check if there is any mismatch in data 
 *  if there is any mismatch in data then in that case it send alert to Bak.
 *
 */

public class PricePublishMonitor{
	
	private PriceUpdateService updateService;
	private AlertService alertService;
	private PricePublisher publisher;
	private PriceMonitor monitor;
	
	public PricePublishMonitor(PriceUpdateService updateService,AlertService alertService){
		this.updateService = updateService;
		this.alertService = alertService;
		publisher = new PricePublisher();
		this.updateService.subscribeToBankPriceUpdates(publisher);
		monitor = new PriceMonitor();
		this.updateService.subscribeToCompanyPriceUpdates(monitor);
	}

	public List<String> compare(){
		Map<String,Double> thirdPartyMap = publisher.bankData;
		List<String> errorMessages = new ArrayList<>();
		Map<String,Double> clientMap = monitor.clientData;
			for(Entry<String,Double> entry : thirdPartyMap.entrySet()){
				String symbol = entry.getKey();
				Double priceSentToClient = clientMap.get(symbol);
				Double priceByBank = entry.getValue();
				if(priceSentToClient == null || !priceSentToClient.equals(priceByBank)){
					errorMessages.add("Price Mismatch for " + symbol) ;
				}
				
			}
		
		return errorMessages;
	}
	
	public void monitor(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  public void run() {
				  List<String> errorMessages = compare();
				  if(errorMessages.size()>0){
					  for(int i =0;i<errorMessages.size();i++)
						  alertService.alert(errorMessages.get(i));
				  }
			  }
			},30*1000L, 30*1000L);
	
		
	}
	
	/**
	 * Price Publisher will collect the data that has been sent by Bank to ThirdParty
	 * It stores the data in expiry map. This map has scheduled task which delete element every second
	 *  whose age greater than 30 sec.
	 */
	private class PricePublisher implements PriceListener{
		
		private Map<String,Double>  bankData = new HashMap<String, Double>();

		@Override
		public void priceUpdate(String symbol, double price) {
			bankData.put(symbol, price);
		}

	}
	
	/**
	 * Price Publisher will collect the data that has been sent by thirdParty to bank
	 * It stores the data in  Hashmap. 
	 **/
	 
	private class PriceMonitor implements PriceListener{
		
		private Map<String,Double>  clientData = new HashMap<String, Double>();

		@Override
		public void priceUpdate(String symbol, double price) {
			clientData.put(symbol, price);
		}

		
	}

	public static void main(String[] str) throws InterruptedException{
		ThirdPartyMap map = new ThirdPartyMap();
		map.put("asset1",1);
		Thread.sleep(1000);
		map.put("asset1",2);
		System.out.println(map.get("asset1"));
	}
}
