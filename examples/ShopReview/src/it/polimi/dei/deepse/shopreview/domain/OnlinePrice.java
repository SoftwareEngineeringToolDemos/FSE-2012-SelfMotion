package it.polimi.dei.deepse.shopreview.domain;

public class OnlinePrice {

	private String store;
	private double price;
	private String currency;
	
	public OnlinePrice(String store, double price, String currency) {
		super();
		this.store = store;
		this.price = price;
		this.currency = currency;
	}
	
	public String getStore() {
		return store;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OnlinePrice other = (OnlinePrice) obj;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		return true;
	}
	
	
	
}
