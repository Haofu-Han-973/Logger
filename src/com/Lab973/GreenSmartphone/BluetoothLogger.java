package com.Lab973.GreenSmartphone;

import android.bluetooth.BluetoothAdapter;

public class BluetoothLogger extends MyLogger{
	protected BluetoothAdapter m_btAdapter;
	public BluetoothLogger(String logFileName, int interval) {
		super(logFileName, interval);
		m_btAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public String getLogValue() {
		if(m_btAdapter == null)
			return "No Device";
		else{
			String rstr = "" + getStateStr(m_btAdapter.getState()) + " " + getScanModeStr(m_btAdapter.getScanMode()) + " " + m_btAdapter.isDiscovering();
			return rstr;
		}
		
	}
	
	private String getStateStr(int code){
		switch(code){
		case 10: return "STATE_OFF";
		case 12: return "STATE_ON";
		case 13: return "STATE_TURNING_OFF";
		case 11: return "STATE_TURNING_ON";
		default: return "UNKNOWN_CODE";
		}
	}
	
	private String getScanModeStr(int code){
		switch(code){
		case 21: return "SCAN_MODE_CONNECTABLE";
		case 23: return "SCAN_MODE_CONNECTABLE_DISCOVERABLE";
		case 20: return "SCAN_MODE_NONE";
		default: return "UNKNOWN_CODE";
		}
	}
	
}
