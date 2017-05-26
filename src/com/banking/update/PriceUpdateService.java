package com.banking.update;

import java.util.List;

import com.banking.listener.PriceListener;

public interface PriceUpdateService {
	/**
     * Register a PriceListener to receive the prices sent from the bank to
     * the 3rd party company.
     * 
     * @param priceListener listener for bank prices
     */
    void subscribeToBankPriceUpdates(PriceListener priceListener);

    /**
     * Register a PriceListener to receive the prices sent from the 3rd party
     * company to the clients
     * 
     * @param priceListener listener for publisher prices
     */
    void subscribeToCompanyPriceUpdates(PriceListener priceListener);
    
    public List<PriceListener> getThirdPartyList();
    public List<PriceListener> getClientList();


}
