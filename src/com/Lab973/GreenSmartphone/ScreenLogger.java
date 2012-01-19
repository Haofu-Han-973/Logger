
package com.Lab973.GreenSmartphone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
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
	public Context context;
	public static final String TAG="ScreenLogger";
	public ScreenLogger(String logFileName, int interval, Context a) {
		super(logFileName, interval);
		context = a;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		String cmd = "cat /sys/class/leds/lcd-backlight/brightness";
		java.lang.Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()),cmd.length());
			String ret = input.readLine();
			if(ret != null)
			{
				return ret;
			}else //In case there is no /sys/class/leds/lcd-backlight/brightness file, use system setting value
			{
				return ""+Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
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
