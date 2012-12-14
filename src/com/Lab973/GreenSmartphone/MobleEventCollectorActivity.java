package com.Lab973.GreenSmartphone;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MobleEventCollectorActivity extends Activity {
    /** Called when the activity is first created. */
	Button buttonstart=null;
	Button buttonstop=null;
	Button buttonstamp = null;
	Button bLeft,bRight,bPool;
	SeekBar intervalbar=null;
	CheckBox cbscreen=null;
	CheckBox cbcpu=null;
	CheckBox cbmemory=null;
	CheckBox cbpacket=null;
	CheckBox cbthreeG=null;
	CheckBox cbwifi=null;
	CheckBox cbbluetooth=null;
	CheckBox cbgps=null;
	CheckBox cbsensor=null;
	CheckBox cbtouch=null;
	CheckBox cbgyro=null;
	
	boolean screenchecked=false;
	boolean cpuchecked=false;
	boolean memorychecked=false;
	boolean packetchecked=false;
	boolean threeGchecked=false;
	boolean wifichecked=false;
	boolean bluetoothchecked=false;
	boolean gpschecked=false;
	boolean sensorchecked = false;
	boolean touchchecked=false;
	boolean gyrochecked=false;
	
	int interval=1;
	PrintWriter time_stamp_file = null;
	SimpleDateFormat bartDateFormat = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        
    	

        setContentView(R.layout.main);
        buttonstart=(Button)findViewById(R.id.buttonstart);
        buttonstop=(Button)findViewById(R.id.buttonstop);
        buttonstart.setOnClickListener(new oclstart());
        buttonstop.setOnClickListener(new oclstop());
        buttonstamp = (Button)findViewById(R.id.button_stamp);
        buttonstamp.setOnClickListener(new oclstamp());
        bLeft = (Button)findViewById(R.id.buttonleft);
        bRight = (Button)findViewById(R.id.buttonright);
        bPool = (Button)findViewById(R.id.buttonpool);
        bLeft.setOnClickListener(new oclstamp());
        bRight.setOnClickListener(new oclstamp());
        bPool.setOnClickListener(new oclstamp());
        
        cbgyro=(CheckBox)findViewById(R.id.checkGyro);
        cbgyro.setOnCheckedChangeListener(new gyrooccl());
        cbtouch=(CheckBox)findViewById(R.id.checkBoxTouch);
        cbscreen=(CheckBox)findViewById(R.id.checkBoxScreenLight);
        cbsensor = (CheckBox)findViewById(R.id.checkBoxSensor);
        cbcpu=(CheckBox)findViewById(R.id.checkBoxCpu);
        cbmemory=(CheckBox)findViewById(R.id.checkBoxMemory);
        cbpacket=(CheckBox)findViewById(R.id.checkBoxPacket);
        cbthreeG=(CheckBox)findViewById(R.id.checkBoxThreeG);
        cbwifi=(CheckBox)findViewById(R.id.checkBoxWifi);
        cbbluetooth=(CheckBox)findViewById(R.id.checkBoxBluetooth);
        cbgps=(CheckBox)findViewById(R.id.checkBoxGPS);
        cbtouch.setOnCheckedChangeListener(new touchoccl());
        cbscreen.setOnCheckedChangeListener(new screenoccl());
        cbcpu.setOnCheckedChangeListener(new cpuoccl());
        cbmemory.setOnCheckedChangeListener(new memoryoccl());
        cbpacket.setOnCheckedChangeListener(new packetoccl());
        cbthreeG.setOnCheckedChangeListener(new threeGoccl());
        cbsensor.setOnCheckedChangeListener(new sensoroccl());
        cbwifi.setOnCheckedChangeListener(new wifioccl());
        cbbluetooth.setOnCheckedChangeListener(new bluetoothoccl());
        cbgps.setOnCheckedChangeListener(new gpsoccl());
        intervalbar = (SeekBar)findViewById(R.id.seekBarScreenLight);
        intervalbar.setOnSeekBarChangeListener(new osb());
        intervalbar.getProgress();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop()
    {
    	
    	if(time_stamp_file != null)
		{
			time_stamp_file.close();
			time_stamp_file = null;
		}
    	super.onStop();
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	if(time_stamp_file == null)
		{
			try {
				time_stamp_file = new PrintWriter(new BufferedWriter(new FileWriter("/sdcard/Log/timestamp.txt",true)));
			} catch (IOException e) {
				Log.e("AndroidLogger", "onResume Failed" + e.toString());
			}
		}
    }
    public void setequal(boolean isChecked,boolean value)
    {
		if(isChecked)value=true;
		else value=false;
    }
    
    class oclstart implements OnClickListener
    {
		@Override
		public void onClick(View v) {
			interval=intervalbar.getProgress();
			Intent intent=new Intent();
			intent.setClass(MobleEventCollectorActivity.this, LogService.class);
			intent.putExtra("Screen", screenchecked);
			intent.putExtra("Memory", memorychecked);
			intent.putExtra("Cpu", cpuchecked);
			intent.putExtra("Packet", packetchecked);
			intent.putExtra("ThreeG", threeGchecked);
			intent.putExtra("Wifi", wifichecked);
			intent.putExtra("Bluetooth", bluetoothchecked);
			intent.putExtra("GPS", gpschecked);
			intent.putExtra("Interval", interval);
			intent.putExtra("Sensor", sensorchecked);
			intent.putExtra("Touch", touchchecked);
			intent.putExtra("Gyro", gyrochecked);
		
			startService(intent);
			
			buttonstart.setEnabled(false);
			buttonstop.setEnabled(true);
		}    	
    }
    class oclstamp implements OnClickListener
    {
		@Override
		public synchronized void onClick(View v) {
			try {
				if(time_stamp_file == null)
				{
					time_stamp_file = new PrintWriter(new BufferedWriter(new FileWriter("/sdcard/Log/timestamp.txt",true)));
				}
				if(bartDateFormat == null)
				{
					bartDateFormat =  new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
				}
				long timeInMillis = System.currentTimeMillis();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(timeInMillis);
				Date date = cal.getTime();
				time_stamp_file.println(bartDateFormat.format(date) + "\t" + ((Button)v).getText());
				time_stamp_file.flush();
			} catch (IOException e) {
				Log.e("AndroidLogger", "time stamp create failed " + e.toString());
			}
			
		}
    }
    class oclstop implements OnClickListener
    {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(MobleEventCollectorActivity.this, LogService.class);
			stopService(intent);
			
			buttonstart.setEnabled(true);
			buttonstop.setEnabled(false);
		}    	
    }
    class osb implements OnSeekBarChangeListener
    {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			TextView tv = (TextView)findViewById(R.id.textViewScreen);    		
    		//interval=seekBar.getProgress()+1;
    		tv.setText("Interval:"+(seekBar.getProgress()+1)+"ms");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {	
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
    	
    }
    class screenoccl implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)screenchecked=true;
			else screenchecked=false;	
		}   	
    }
    class gyrooccl implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)gyrochecked=true;
			else gyrochecked=false;	
		}   	
    }
    class touchoccl implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)touchchecked=true;
			else touchchecked=false;	
		}   	
    }
    class cpuoccl implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)cpuchecked=true;
			else cpuchecked=false;
		}   	
    }
    class memoryoccl implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)memorychecked=true;
			else memorychecked=false;		
		}   	
    }
    class packetoccl implements OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)packetchecked=true;
			else packetchecked=false;		
		}   	
    }
    class threeGoccl implements OnCheckedChangeListener
    {
    	@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)threeGchecked=true;
			else threeGchecked=false;		
		}   	
    }
    class sensoroccl implements OnCheckedChangeListener
    {
    	@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)sensorchecked=true;
			else sensorchecked=false;		
		}   	
    }
    class wifioccl implements OnCheckedChangeListener
    {
    	@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)wifichecked=true;
			else wifichecked=false;		
		}   	
    }
    class bluetoothoccl implements OnCheckedChangeListener
    {
    	@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)bluetoothchecked=true;
			else bluetoothchecked=false;		
		}   	
    }
    class gpsoccl implements OnCheckedChangeListener
    {
    	@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)gpschecked=true;
			else gpschecked=false;		
		}   	
    }
}