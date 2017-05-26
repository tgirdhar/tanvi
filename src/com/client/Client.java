package com.client;

import java.util.HashMap;
import java.util.Map;

import com.banking.listener.PriceListener;

public class Client implements PriceListener{
	Map< String, Double> map;
	
	public Client() {
		map = new HashMap<>();
		
	}

	@Override
	public void priceUpdate(String symbol, double price) {
		map.put(symbol, price);
		
	}

}
