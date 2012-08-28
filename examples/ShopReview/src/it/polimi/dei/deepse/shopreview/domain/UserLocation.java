package it.polimi.dei.deepse.shopreview.domain;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

public class UserLocation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5210798735461942624L;
	
	private double latitude;
	private double longitude;
	
	public UserLocation(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public UserLocation(GeoPoint selectedLocation) {
		this.latitude = selectedLocation.getLatitudeE6()/1E6;
		this.longitude = selectedLocation.getLongitudeE6()/1E6;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public GeoPoint toGeoPoint() {
		return new GeoPoint((int)(latitude*1E6), (int)(longitude*1E6));
	}
	
	@Override
	public String toString() {
		return latitude+","+longitude;
	}
}
