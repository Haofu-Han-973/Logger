package com.Lab973.GreenSmartphone;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AndroidLoggerActivity extends Activity {
    /** Called when the activity is first created. */
	private Button startButton;
	private Button stopButton;
	private HashMap<String, MyLogger> loggers;
	
	@Override
	public void onDestroy()
	{
		Iterator<String> it = loggers.keySet().iterator();
    	while(it.hasNext())
    	{
    		loggers.get(it.next()).stopLog();
    	}
        loggers.clear();
		super.onDestroy();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity act = (Activity)this;
        setContentView(R.layout.main);
        startButton = (Button)findViewById(R.id.buttonStart);
        stopButton = (Button)findViewById(R.id.buttonStop);
        loggers = new HashMap<String, MyLogger>();
        
        SeekBar skScreen = (SeekBar)findViewById(R.id.seekBarScreenLight);
        skScreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){ 
        	public void onProgressChanged(SeekBar seekBar, int progress,
    				boolean fromUser) {
        		TextView tv = (TextView)findViewById(R.id.textViewScreen);
        		tv.setText("Interval:"+(seekBar.getProgress()+1)+"ms");
    		}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
        });
        
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(((CheckBox)findViewById(R.id.checkBoxScreenLight)).isChecked())
            	{
            		loggers.put("Screen", new ScreenLogger("/sdcard/screenlog.txt", ((SeekBar)findViewById(R.id.seekBarScreenLight)).getProgress()+1, (Activity)act));
            	}
            	//TODO finish other checkboxes
            	Iterator<String> it = loggers.keySet().iterator();
            	while(it.hasNext())
            	{
            		loggers.get(it.next()).startLog();
            	}
            	((Button)findViewById(R.id.buttonStop)).setEnabled(true);
            	((Button)findViewById(R.id.buttonStart)).setEnabled(false);
            }
        });
        
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Iterator<String> it = loggers.keySet().iterator();
            	while(it.hasNext())
            	{
            		loggers.get(it.next()).stopLog();
            	}
                loggers.clear();
            	((Button)findViewById(R.id.buttonStop)).setEnabled(false);
            	((Button)findViewById(R.id.buttonStart)).setEnabled(true);
            }
        });
        stopButton.setEnabled(false);
    }

}