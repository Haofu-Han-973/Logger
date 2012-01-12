package com.Lab973.GreenSmartphone;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
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
	SeekBar intervalbar=null;
	CheckBox cbscreen=null;
	CheckBox cbcpu=null;
	CheckBox cbmemory=null;
	CheckBox cbpacket=null;
	boolean screenchecked=false;
	boolean cpuchecked=false;
	boolean memorychecked=false;
	boolean packetchecked=false;
	int interval=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    	

        setContentView(R.layout.main);
        buttonstart=(Button)findViewById(R.id.buttonstart);
        buttonstop=(Button)findViewById(R.id.buttonstop);
        buttonstart.setOnClickListener(new oclstart());
        buttonstop.setOnClickListener(new oclstop());
        
        cbscreen=(CheckBox)findViewById(R.id.checkBoxScreenLight);
        cbcpu=(CheckBox)findViewById(R.id.checkBoxCpu);
        cbmemory=(CheckBox)findViewById(R.id.checkBoxMemory);
        cbpacket=(CheckBox)findViewById(R.id.checkBoxPacket);
        cbscreen.setOnCheckedChangeListener(new screenoccl());
        cbcpu.setOnCheckedChangeListener(new cpuoccl());
        cbmemory.setOnCheckedChangeListener(new memoryoccl());
        cbpacket.setOnCheckedChangeListener(new packetoccl());
        
        intervalbar = (SeekBar)findViewById(R.id.seekBarScreenLight);
        intervalbar.setOnSeekBarChangeListener(new osb());
        intervalbar.getProgress();
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
			intent.putExtra("Interval", interval);
			
			startService(intent);
			
			buttonstart.setEnabled(false);
			buttonstop.setEnabled(true);
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
}