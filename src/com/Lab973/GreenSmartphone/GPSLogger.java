package com.Lab973.GreenSmartphone;

// Not finished

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.app.Service;
public class GPSLogger extends MyLogger{
	
	protected LocationManager m_locManager;
	protected double n_latitude = 0, n_longitude = 0, g_latitude = 0,g_longitude = 0,altitude = 0,speed = 0,accuracy = 0;
	protected Service m_a;
	protected String provider;
	protected LocationListener gps_listener,network_listener;
	public GPSLogger(String logFileName, int interval,Service a) {
		super(logFileName, interval);
		m_a = a;				
		m_locManager = (LocationManager)a.getSystemService(Context.LOCATION_SERVICE);
		gps_listener = new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location) {
				g_latitude = location.getLatitude();
			    g_longitude = location.getLongitude();
			}
			@Override
			public void onProviderDisabled(String provider) {
			}
			@Override
			public void onProviderEnabled(String provider) {
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};
		network_listener = new LocationListener()
		{

			@Override
			public void onLocationChanged(Location location) {
				n_latitude = location.getLatitude();
			    n_longitude = location.getLongitude();
				
			}
			@Override
			public void onProviderDisabled(String provider) {
			}
			@Override
			public void onProviderEnabled(String provider) {
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};
		try{
		m_locManager.requestLocationUpdates(m_locManager.GPS_PROVIDER, 1000, 5, gps_listener);
		m_locManager.requestLocationUpdates(m_locManager.NETWORK_PROVIDER, 1000, 10, network_listener);
		}catch(Exception e)
		{
			Log.e("GPSLogger", e.toString());
		}
	}
	@Override
	public void stopLog()
	{
		m_locManager.removeUpdates(gps_listener);
		m_locManager.removeUpdates(network_listener);
		super.stopLog();
	}
	
	@Override
	public String getLogValue() {
		if(m_locManager == null)
			return "No Deivce";
		else{
			boolean isGPSEnabled = m_locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean isNETEnabled = m_locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			String rstr = "GPS" ;
			
			if(isGPSEnabled)
			{
				rstr += " " + g_latitude + " " + g_longitude;
			}else
			{
				rstr += " 0.0 0.0";
			}
			rstr += " NETWORK" ;
			if(isNETEnabled)
			{
				rstr += " " + n_latitude + " " + n_longitude;
			}else
			{
				rstr += " 0.0 0.0";
			}
			return rstr;
		}
		
	}
	
	
}
