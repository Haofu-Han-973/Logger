/**
 * 
 */
package com.Lab973.GreenSmartphone;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

/**
 * @author hhf
 *
 */
public class ScreenLogger extends MyLogger {

	/**
	 * @param logFileName
	 */
	public ScreenLogger(String logFileName, int interval, Activity a) {
		super(logFileName, interval, a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		String cmd = "cat /sys/class/leds/lcd-backlight/brightness";
		java.lang.Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.getInputStream();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()),cmd.length());
			String ret = input.readLine();
			if(ret != null)
			{
				return ret;
			}else
			{
				return ""+Settings.System.getInt(this.activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
