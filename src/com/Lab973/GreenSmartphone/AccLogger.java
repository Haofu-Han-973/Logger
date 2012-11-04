package com.Lab973.GreenSmartphone;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.hardware.SensorManager;
import android.util.Log;


public class AccLogger extends MyLogger implements SensorEventListener {
	BufferedWriter out;
	SimpleDateFormat bartDateFormat;
	private Sensor mAccelerometer;
	private Sensor mOrientation;
	private SensorManager sensorManager;
	private String log;
	private StringBuilder log_perf = null;
	private int count = 0;
	private float orientation[] = {0,0,0};
	public AccLogger(String logFileName, int interval, SensorManager s) {
		super(logFileName, interval);
		try{
			log = "";
			log_perf = new StringBuilder();
			out = new BufferedWriter(new FileWriter(this.logFileName));
		    bartDateFormat =  new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");  
		    sensorManager = s;
		    mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		    mOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		    sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		    sensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_FASTEST);
		    
		}catch(IOException e)
		{
			Log.e("SensorLogger",e.toString());
			
		}
	}
	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		try {
			this.stopFlag = true;
		  
			while(stopFlag){
				try {
				
					Thread.sleep(1000);
					
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			if(!stopFlag)
			{
				sensorManager.unregisterListener(this);
				out.write(log_perf.toString());
				out.flush();
				out.close();
				
			}
		} catch (IOException e) {//open file failed!
			e.printStackTrace();
		}
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent e) {
		// TODO Auto-generated method stub
		switch(e.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:{
			long timeInMillis = (long) (e.timestamp * 1.0f / 1000000.0f);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timeInMillis);
			Date date = cal.getTime();
	
			float x = e.values[SensorManager.DATA_X];
	        float y = e.values[SensorManager.DATA_Y];
	        float z = e.values[SensorManager.DATA_Z];
	        log_perf.append(bartDateFormat.format(date)+"\t"+ x + " " + y + " " + z + " "
	        	+ orientation[0] + " " + orientation[1] + " " + orientation[2] + "\r\n");
	        if(count++ >= FLUSH_COUNT)
	        {
	        	count = 0;
	        	try {
					out.flush();
					out.write(log_perf.toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Log.e("SensorLogger", e1.toString());
				}
	        	log_perf = new StringBuilder();
	        }
	        break;
		}
		case Sensor.TYPE_ORIENTATION:{
			orientation[0] = e.values[SensorManager.DATA_X];//azimuth
			orientation[1] = e.values[SensorManager.DATA_Y];//pitch
			orientation[2] = e.values[SensorManager.DATA_Z];//roll
			break;
		}
		}
	}


}
