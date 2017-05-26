package com.thirdParty;

import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.banking.listener.PriceListener;
import com.banking.update.PriceUpdateService;
import com.thirdParty.ThirdPartyMap.Node;

/**
 * 
 * @author tgirdh
 * 
 * ThirdParty class has been receiving prices from bank and keeping them with its own implementation of ThirdPartyMap
 * This map also maintain time on which put being called along with symbol which helps to know if price for the symbol 
 * is recieved in last 30 second or not.
 *
 */

public class ThirdParty  implements PriceListener{
	
	ThirdPartyMap  priceMap;
	PriceUpdateService service;
	
	public ThirdParty(PriceUpdateService service){
		priceMap = new ThirdPartyMap();
		this.service = service;
	}
	
	
	/*
	 * This method will throttle prices to its listener in every 30 second, 
	 * only symbol for which prices has been sent by bank in last 30 second 
	 * will be sent to client
	 */
	public void throttle() throws Exception{
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  public void run() {
				  long time = System.currentTimeMillis();
				  for(Entry<Node,Double> entry : priceMap.getMap().entrySet()){
					  long createdTime = entry.getKey().getCreatedDate().getTime();
					  long diff = (time - createdTime)/1000;
					  if(diff<=30){
						  List<PriceListener> clientList = service.getClientList();
						  for(int i =0;i<clientList.size();i++){
							  PriceListener listener = clientList.get(i);
							  listener.priceUpdate(entry.getKey().getSymbol(), entry.getValue());
						  }
					  }
				  }
			  }
			}, 30*1000L);
	}
	
	
	@Override
	public void priceUpdate(String symbol, double price) {
		priceMap.put(symbol, price);
	}
	
	

}
