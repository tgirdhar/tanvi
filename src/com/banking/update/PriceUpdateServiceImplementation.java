package com.banking.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.banking.listener.PriceListener;

public class PriceUpdateServiceImplementation implements PriceUpdateService {
	
	public List<PriceListener> thirdPartyList;
	public List<PriceListener> clientList;
	
	public PriceUpdateServiceImplementation(){
		thirdPartyList = new ArrayList<>();
		clientList = new ArrayList<>();
	}

	@Override
	public void subscribeToBankPriceUpdates(PriceListener priceListener) {
		thirdPartyList.add(priceListener);
		
	}

	@Override
	public void subscribeToCompanyPriceUpdates(PriceListener priceListener) {
		clientList.add(priceListener);		
	}

	public List<PriceListener> getThirdPartyList() {
		return Collections.unmodifiableList(thirdPartyList);
	}

	public List<PriceListener> getClientList() {
		return Collections.unmodifiableList(clientList);
	}

}
