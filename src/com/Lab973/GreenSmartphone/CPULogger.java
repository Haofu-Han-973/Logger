package com.Lab973.GreenSmartphone;

import java.io.BufferedWriter;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CPULogger extends MyLogger{
	String log="";
	public CPULogger(String logFileName, int interval) {
		super(logFileName, interval);
	}

	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		double cpuused=0;
		String[] toks=null;
		
		try 
		{ 
			RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");			   
		
			String load = reader.readLine();			   
			toks = load.split(" ");			   
			long idle1 = Long.parseLong(toks[5]);			   
			long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) +Long.parseLong(toks[5])+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);   
			
			Thread.sleep(this.logInterval);   
			reader.seek(0);
			   
			load = reader.readLine();  
			reader.close();			   
			toks = load.split(" ");			   
			long idle2 = Long.parseLong(toks[5]);		   
			long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[5])+Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);			   
			cpuused=100*((cpu2-cpu1 )-(idle2-idle1))/ (cpu2-cpu1 );
			return cpuused+"%";
			} catch (IOException ex) { ex.printStackTrace();} catch (InterruptedException e) {
				e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.stopFlag = true;
		    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/sdcard/cpu.txt",false)));
		    SimpleDateFormat bartDateFormat =  
		    		  new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");  
			while(stopFlag){
				long timeInMillis = System.currentTimeMillis();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(timeInMillis);
				Date date = cal.getTime();
				log+=bartDateFormat.format(date)+"\t"+ this.getLogValue() + "\r\n";
			}
			if(!stopFlag)
			{
				out.write(log);
				out.close();
			}
		} catch (IOException e) {//open file failed!
			e.printStackTrace();
		}
	}

}
