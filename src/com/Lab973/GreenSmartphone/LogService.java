package com.Lab973.GreenSmartphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class LogService extends Service {
	//CpuThread ct;
	int interval=1;
	boolean screenchecked=false;
	boolean cpuchecked=false;
	boolean memorychecked=false;
	boolean packetchecked=false;
	CPULogger cl;
	ScreenLogger sl;
	MemoLogger m1;
	NotificationManager mNM;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		 
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
		destroyNotification();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		interval=intent.getIntExtra("Interval", 1000);
		cpuchecked=intent.getBooleanExtra("Cpu", false);
		screenchecked=intent.getBooleanExtra("Screen", false);
		memorychecked=intent.getBooleanExtra("Memory", false);
		
		if(cpuchecked)
		{
			cl=new CPULogger("/sdcard/cpu.txt", interval);	
			cl.start();
		}
		if(screenchecked)
		{
			sl=new ScreenLogger("/sdcard/screen.txt", interval, getApplicationContext());	
			sl.start();
		}
		if(memorychecked)
		{
			m1=new MemoLogger("/sdcard/memory.txt", interval, this);
			m1.start();
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
