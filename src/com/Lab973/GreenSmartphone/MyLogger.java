/**
 * 
 */
package com.Lab973.GreenSmartphone;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
/**
 * @author hhf
 *
 */
public abstract class MyLogger extends Thread {
	public String logFileName;
	public int logInterval; // ms
	public Activity activity;
	public int stopFlag;
	public MyLogger(String logFileName, int interval, Activity a){
		super();
		this.logFileName = logFileName;
		this.logInterval = interval;
		this.activity = a;
		this.stopFlag = 0;
	}
	public void startLog()
	{
		this.start();
	}
	public void stopLog()
	{
		this.stopFlag = 1;
	}
	public abstract String getLogValue();
	public void run() {
		try {
			this.stopFlag = 0;
		    BufferedWriter out = new BufferedWriter(new FileWriter(this.logFileName));
		    SimpleDateFormat bartDateFormat =  
		    		  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  
			while(true){
				try {
					Thread.sleep(this.logInterval);
					
					if(this.stopFlag == 1)
					{
						out.close();
						break;
						
					}else{
						long timeInMillis = System.currentTimeMillis();
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(timeInMillis);
						Date date = cal.getTime();
						out.write(bartDateFormat.format(date)+"\t"+ this.getLogValue() + "\n");
						out.flush();
					}
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		
		} catch (IOException e) {//open file failed!
			e.printStackTrace();
		}

	}

}
