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
public class Bank {
	
	Map<String,Double> priceMap;
    PriceUpdateService service;
    
	public Bank(PriceUpdateService service){
		priceMap = new HashMap<>();
		this.service = service;
		for(int i =1;i<=100;i++){
			Double d = new Double(i);
			priceMap.put("asset"+i, d);
		}
	}
	
	
	public void send(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			  public void run() {
				  List<Entry<String, Double>> list = new ArrayList<>(priceMap.entrySet());
				  for(int j = 0;j < 10;j++){
					  int index = (int)(Math.random()*j);
					  Entry<String,Double> entry = list.get(index);
					  List<PriceListener> thirdPartyList = service.getThirdPartyList();
					  for (int i = 0; i < thirdPartyList.size(); i++) {
						  PriceListener priceListener = thirdPartyList.get(i);
						  priceListener.priceUpdate(entry.getKey(), entry.getValue());
					}
				  }
			  }
			}, 10*1000L);
	}
}
