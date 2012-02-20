package com.Lab973.GreenSmartphone;

// Not finished

import android.content.Context;
import android.location.LocationManager;
import android.app.Service;
public class GPSLogger extends MyLogger{
	
	protected LocationManager m_locManager;
	
	protected Service m_a;
	public GPSLogger(String logFileName, int interval,Service a) {
		super(logFileName, interval);
		m_a = a;				
		m_locManager = (LocationManager)a.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public String getLogValue() {
		if(m_locManager == null)
			return "No Deivce";
		else{
			boolean isEnabled = m_locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			String rstr = "" + isEnabled;
			return rstr;
		}
		
	}
	
}
