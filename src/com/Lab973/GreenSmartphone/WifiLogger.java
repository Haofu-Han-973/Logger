package com.Lab973.GreenSmartphone;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.app.Service;

public class WifiLogger extends MyLogger{
	protected WifiManager m_wifiManager;
	protected WifiInfo m_wifiInfo;
	protected Service m_service;
	public WifiLogger(String logFileName, int interval, Service s) {
		super(logFileName, interval);
		m_service = s;
		m_wifiManager = (WifiManager)m_service.getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	public String getLogValue() {
		if(m_wifiManager == null)
			return "No Device";
		else{
			m_wifiInfo = m_wifiManager.getConnectionInfo();
			String rstr = "" + getWifiStateStr(m_wifiManager.getWifiState()) + " " + m_wifiInfo.getRssi();
			return rstr;
		}	
	}
	
	private String getWifiStateStr(int code){
		switch(code){
		case 1: return "WIFI_STATE_DISABLED";
		case 0: return "WIFI_STATE_DISABLING";
		case 3: return "WIFI_STATE_ENABLED";
		case 2: return "WIFI_STATE_ENABLING";
		default: return "UNKNOWN CODE";
		}
	}

}
