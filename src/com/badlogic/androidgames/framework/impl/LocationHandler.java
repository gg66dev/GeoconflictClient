package com.badlogic.androidgames.framework.impl;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationHandler implements LocationListener {

	double latitud;
	double longitud;
	boolean isChanged;
	
	private LocationManager lm;
	
	public LocationHandler(Context context){
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		latitud = 0.0;
		longitud = 0.0;
		isChanged = false;
	}
	
	
	public double getLatitud(){
		return latitud;
	}
	public double getLongitud(){
		return longitud;
	}

	public boolean isLocationChanged(){
		return isChanged;
	}
	public void setLocationChanged(boolean value){
		  this.isChanged = value;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		latitud = location.getLatitude();
		longitud = location.getLongitude();
		isChanged = true;
		Log.d("debug","nueva localizacion");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
