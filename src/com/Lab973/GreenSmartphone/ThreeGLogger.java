package com.Lab973.GreenSmartphone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.gsm.GsmCellLocation;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.app.Service;


public class ThreeGLogger extends MyLogger {
	protected TelephonyManager m_telManager;
	protected MyPhoneStateListener m_myListener;
	protected Service m_service;
	protected int m_ss;
	public ThreeGLogger(String logFileName, int interval, Service s) {
		super(logFileName, interval);
		m_service = s;
		m_ss = -1;
		m_myListener = new MyPhoneStateListener();
		m_telManager = (TelephonyManager)m_service.getSystemService(Context.TELEPHONY_SERVICE);
		m_telManager.listen(m_myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	@Override
	public String getLogValue() {
		GsmCellLocation gcl = ((GsmCellLocation)m_telManager.getCellLocation());
		String rstr = "" + getNetworkTypeStr(m_telManager.getNetworkType()) + " " + getDataStateStr(m_telManager.getDataState()) + " " + getDataActivityStr(m_telManager.getDataActivity())+ " " + m_ss 
		+ " " + gcl.getCid() + " " + gcl.getLac(); //FIXME GSM_ONLY method, get cell_id and Location_area_code
		return rstr;
		
	}
	private class MyPhoneStateListener extends PhoneStateListener{

		@Override
	    public void onSignalStrengthsChanged(SignalStrength signalStrength){
			super.onSignalStrengthsChanged(signalStrength);
			m_ss = signalStrength.getGsmSignalStrength();
		}
	}
	private String getNetworkTypeStr(int code){
		switch(code){
		case 7: return "NETWORK_TYPE_1xRTT";
		case 4: return "NETWORK_TYPE_CDMA";
		case 2: return "NETWORK_TYPE_EDGE";
		case 14: return "NETWORK_TYPE_EHRPD";
		case 5: return "NETWORK_TYPE_EVDO_0";
		case 6: return "NETWORK_TYPE_EVDO_A";
		case 12: return "NETWORK_TYPE_EVDO_B";
		case 1: return "NETWORK_TYPE_GPRS";
		case 8: return "NETWORK_TYPE_HSDPA";
		case 10: return "NETWORK_TYPE_HSPA";
		case 15: return "NETWORK_TYPE_HSPAP";
		case 9: return "NETWORK_TYPE_HSUPA";
		case 11: return "NETWORK_TYPE_IDEN";
		case 13: return "NETWORK_TYPE_LTE";
		case 3: return "NETWORK_TYPE_UMTS";
		case 0: return "NETWORK_TYPE_UNKNOWN";
		default: return "UNKNOWN_CODE";
		}
	}
	
	private String getDataStateStr(int code){
		switch(code){
		case 2: return "DATA_CONNECTED";
		case 1: return "DATA_CONNECTING";
		case 0: return "DATA_DISCONNECTED";
		case 3: return "DATA_SUSPENDED";
		default: return "UNKNOWN_CODE";
		}
	}
	
	private String getDataActivityStr(int code){
		switch(code){
		case 4: return "DATA_ACTIVITY_DORMANT";
		case 1: return "DATA_ACTIVITY_IN";
		case 3: return "DATA_ACTIVITY_INOUT";
		case 0: return "DATA_ACTIVITY_NONE";
		case 2: return "DATA_ACTIVITY_OUT";
		default: return "UNKNOWN_CODE";
		}	
	
	}

}
