package com.banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.banking.listener.PriceListener;
import com.banking.update.PriceUpdateService;

/*
 * As Bank as price map as bank send price for different symbol
 * Also Bank  wants to be client also so Bank also need to implement PriceListener and
 *  clientPriceList has list of prices of different product which bank received as a client
 */
public class Bank implements PriceListener {
	
	Map<String, Double> clientPriceList;
	Map<String,Double> priceMap;
    PriceUpdateService service;
    Map<String, Double> dataSent;
	
	public Map<String, Double> getClientPriceList() {
		return clientPriceList;
	}

	public void setClientPriceList(Map<String, Double> clientPriceList) {
		this.clientPriceList = clientPriceList;
	}

	public Map<String, Double> getPriceMap() {
		return priceMap;
	}

	public void setPriceMap(Map<String, Double> priceMap) {
		this.priceMap = priceMap;
	}

	public Bank(PriceUpdateService service){
		priceMap = new HashMap<>();
		clientPriceList = new HashMap<>();
		dataSent=new HashMap<>();
		this.service = service;
		for(int i =1;i<=100;i++){
			Double d = new Double(i);
			priceMap.put("asset"+i, d);
		}
	}
	
	
	@Override
	public void priceUpdate(String symbol, double price) {
		clientPriceList.put(symbol, price);
	}
	
	public void send(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			  public void run() {
				  List<Entry<String, Double>> list = new ArrayList<>(getPriceMap().entrySet());
				  for(int j = 0;j < 10;j++){
					  int index = (int)Math.random()*j;
					  Entry<String,Double> entry = list.get(index);
					  List<PriceListener> thirdPartyList = service.getThirdPartyList();
					  for (int i = 0; i < thirdPartyList.size(); i++) {
						  PriceListener priceListener = thirdPartyList.get(i);
						  priceListener.priceUpdate(entry.getKey(), entry.getValue());
						  dataSent.put(entry.getKey(), entry.getValue());	
					}
				  }
			  }
			}, 10*1000L);
		//this timer will reset dataSent after 30 second so that it always has data that was sent in last 30 seconds
		Timer resetTimer = new Timer();
		resetTimer.schedule(new TimerTask() {
			public void run() {
				dataSent.clear();
			  }
		},30*1000);
			  

		
	
	}

	public Map<String, Double> getDataSent() {
		return dataSent;
	}
	

}
