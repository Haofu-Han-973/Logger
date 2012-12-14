package com.Lab973.GreenSmartphone;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import com.Lab973.GreenSmartphone.IRemoteService.Stub;


public class LogService extends Service {
	int interval=1;
	boolean started = false;
	boolean screenchecked=false;
	boolean cpuchecked=false;
	boolean memorychecked=false;
	boolean packetchecked=false;
	boolean threeGchecked=false;
	boolean wifichecked=false;
	boolean bluetoothchecked=false;
	boolean gpschecked=false;
	boolean sensorchecked=false;
	boolean touchchecked=false;
	boolean gyrochecked=false;
	GyroLogger gyrol;
	CPULogger cl;
	ScreenLogger sl;
	MemoLogger m1;
	PacketLogger pl;
    ThreeGLogger tl;
	WifiLogger wl;
	BluetoothLogger bl;
	GPSLogger gl;
	AccLogger sensorlogger;
	TouchLogger touchlogger;
	PowerManager.WakeLock wlock;
	NotificationManager mNM;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	private final IRemoteService.Stub mBinder=new Stub() {
		@Override
		public String request(String message) throws RemoteException {
			return "Hello "+message;
		}
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wlock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AndroidLogger");
		wlock.acquire();
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (cpuchecked)
			cl.stopLog();
		if (screenchecked)
			sl.stopLog();
		if (memorychecked)
			m1.stopLog();
		if (packetchecked)
			pl.stopLog();
		if (touchchecked)
			touchlogger.stopLog();
		if (threeGchecked)
			tl.stopLog();
		if (wifichecked)
			wl.stopLog();
		if (bluetoothchecked)
			bl.stopLog();
		if (gpschecked)
			gl.stopLog();
		if (sensorchecked)
			sensorlogger.stopLog();
		if (gyrochecked)
			gyrol.stopLog();
		wlock.release();
		destroyNotification();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if(intent != null && !this.started){
			interval = intent.getIntExtra("Interval", 1000);
			cpuchecked = intent.getBooleanExtra("Cpu", false);
			screenchecked = intent.getBooleanExtra("Screen", false);
			memorychecked = intent.getBooleanExtra("Memory", false);
			packetchecked = intent.getBooleanExtra("Packet", false);
			sensorchecked = intent.getBooleanExtra("Sensor", false);
			threeGchecked = intent.getBooleanExtra("ThreeG", false);
			wifichecked = intent.getBooleanExtra("Wifi", false);
			touchchecked = intent.getBooleanExtra("Touch", false);
			bluetoothchecked = intent.getBooleanExtra("Bluetooth", false);
			gpschecked = intent.getBooleanExtra("GPS", false);
			gyrochecked = intent.getBooleanExtra("Gyro", false);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String curDateStr = formatter.format(curDate);
			if(cpuchecked)
			{
				cl = new CPULogger("/sdcard/Log/cpu/"+curDateStr+".txt", interval);	
	
				cl.start();
			}
			if(screenchecked)
			{
	
				sl = new ScreenLogger("/sdcard/Log/screen/"+curDateStr+".txt", interval, getApplicationContext());	
	
				sl.start();
			}
			if(memorychecked)
			{
	
				m1 = new MemoLogger("/sdcard/Log/memory/"+curDateStr+".txt", interval, this);
	
				m1.start();
			}
			if(packetchecked)
			{
	
				pl = new PacketLogger("/sdcard/Log/packet/"+curDateStr+".txt", interval);
				pl.start();
			}
			if(threeGchecked)
			{
				tl = new ThreeGLogger("/sdcard/Log/threeG/"+curDateStr+".txt", interval, this);
				tl.start();
			}
			if(wifichecked)
			{
				wl = new WifiLogger("/sdcard/Log/wifi/"+curDateStr+".txt", interval, this);
				wl.start();
			}
			if(bluetoothchecked)
			{
				bl = new BluetoothLogger("/sdcard/Log/bluetooth/"+curDateStr+".txt", interval);
				bl.start();
			}
			if(gpschecked)
			{
				gl = new GPSLogger("/sdcard/Log/gps/"+curDateStr+".txt", interval, this);
				gl.start();
			}
			if(sensorchecked)
			{
				sensorlogger = new AccLogger("/sdcard/Log/acc/"+curDateStr+".txt", interval, (SensorManager)this.getSystemService(SENSOR_SERVICE));
				sensorlogger.start();
			}
			if(touchchecked)
			{
				touchlogger = new TouchLogger("/sdcard/Log/touch/"+curDateStr+".txt", interval);
				touchlogger.start();
			}
			if(gyrochecked)
			{
				gyrol = new GyroLogger("/sdcard/Log/gyro/"+curDateStr+".txt", interval, (SensorManager)this.getSystemService(SENSOR_SERVICE));
				gyrol.start();
			}
		}
		super.onStart(intent, startId);
	}
	private void showNotification() {
        Notification notification = new Notification(R.drawable.ic_launcher, "AndroidLogger is Logging...",
                System.currentTimeMillis());
        Intent intent = new Intent(getApplicationContext(),MobleEventCollectorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
        		getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "AndroidLogger",
        		"Logging...", pendingIntent);
        mNM.notify("NOTIFICATION",0, notification);
    }
    private void destroyNotification() {
        mNM.cancel("NOTIFICATION", 0);
    }


}
