package com.thirdParty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;



/**
 * 
 * @author tgirdh
 * 
 * Third party map has Node as key and double as price
 * Node has  symbol as well as created date as parameter
 * now Key has created time with symbol, now it's help other class to know the time when this symbol is recieved or updated.
 * This map has internal thread which runs after 10 seconds and check all the entries whose timestamp > 30 should be removed from map
 *
 */
public class ThirdPartyMap {
	Map<Node, Double> map;
	
	public ThirdPartyMap(){
		map = new HashMap<>();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			  public void run() {
				  long time = System.currentTimeMillis();
				  for(Entry<Node,Double> entry : getMap().entrySet()){
					  long createdTime = entry.getKey().getCreatedDate().getTime();
					  long diff = (time - createdTime)/1000;
					  if(diff>30){
						  getMap().remove(entry.getKey());
					  }
				  }
			  }
			}, 10*1000L);
	}
	
	public  synchronized void put(String symbol,double price){
		Node n = new Node(symbol);
		map.put(n, price);
	}
	
	public synchronized double get(String symbol){
		Node n = new Node(symbol);
		return map.get(n);
	}
	
	public Map<Node, Double> getMap() {
		return map;
	}

	static class Node {
		Date createdDate;
		String symbol;
		public Node(String symbol){
			this.symbol = symbol;
			this.createdDate=new Date();
		}
		public int hashCode(){
			return symbol.hashCode();
		}
		public boolean equals(Object obj){
			if(obj != null && obj instanceof Node){
				Node n = (Node) obj;
				return this.symbol.equals(n.symbol);
			}
			return false;
		}
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		
		
	}

	   
}
