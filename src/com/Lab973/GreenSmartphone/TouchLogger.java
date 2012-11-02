package com.Lab973.GreenSmartphone;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.util.Log;


import com.Lab973.GreenSmartphone.LinuxInputEvent;

public class TouchLogger extends MyLogger {

	public TouchLogger(String logFileName, int interval) {
		super(logFileName, interval);
		
	}
	public void run() {
		java.lang.Process cat = null;

		DataOutputStream output;
		
		InputStream in;
		
		Log.i("TouchLogger", "Start Recording...");
		try {
			this.stopFlag = true;
		    BufferedWriter out = new BufferedWriter(new FileWriter(this.logFileName));
		    int count = 0;
		    cat = Runtime.getRuntime().exec("su");
			OutputStream outs = cat.getOutputStream();
			output = new DataOutputStream(outs);
			output.writeBytes("cat /dev/input/event0\n");
			//event1--Galaxy Nexus
			//event2--Galaxy SII
			output.flush();
			
			in = cat.getInputStream();
			byte buf[] = new byte[16];
			int len = in.read(buf);
			while(len > 0 && (this.stopFlag)){
				try {
					if(count++ == FLUSH_COUNT)
					{
						out.write(log);
						out.flush();
						log = "";
						count = 0;
					}
					
					LinuxInputEvent evt = new LinuxInputEvent(buf);
					log += evt.toLogFile();
					len = in.read(buf);
				} catch (IOException e1) {
					Log.e("TouchLogger",e1.toString());
				}
			}
			if(!stopFlag)
			{
				out.write(log);
				out.flush();
				out.close();
				output.close();
			}
		} catch (IOException e) {//open file failed!
			Log.e("TouchLogger",e.toString());
		}

		Log.i("RecordIt","DONE!");
		

	}
	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
