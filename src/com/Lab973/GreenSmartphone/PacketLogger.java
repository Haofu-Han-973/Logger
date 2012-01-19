/**
 * 
 */
package com.Lab973.GreenSmartphone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.util.Log;

/**
 * @author hhf
 */
public class PacketLogger extends MyLogger {
	private java.lang.Process process;
	public PacketLogger(String logFileName, int interval) {
		super(logFileName, interval);
		process = null;
	}
	private void killTcpdump()
	{
		String ps = "ps | grep tcpdump";
		String killcmd = "kill ";

		try {
			java.lang.Process p = Runtime.getRuntime().exec(ps);
			java.lang.Process kill = Runtime.getRuntime().exec("su");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			input.readLine(); //skip title
			OutputStream outs = kill.getOutputStream();
			DataOutputStream output = new DataOutputStream(outs);
			
			String ret = input.readLine();
			while(ret != null && ret.length() != 0)
			{
				Pattern pt= Pattern.compile("root\\s+(\\d+)");
				Matcher matcher = pt.matcher(ret);
				matcher.lookingAt();
				killcmd += matcher.group(1)+" ";
				ret = input.readLine();
			}
			
			output.writeBytes(killcmd+"\n");
			output.flush();
			output.writeBytes("exit\nexit\n");
			output.flush();
			p.destroy();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Debug",e.getLocalizedMessage());
		}
		Log.e("Debug","done!");
	}
	@Override
	public void run() {
		String cmd = "su";
		try {
			this.process = Runtime.getRuntime().exec(cmd);
			this.stopFlag = true;
			
			OutputStream outs = process.getOutputStream();
			DataOutputStream output = new DataOutputStream(outs);
			output.writeBytes("/data/tcpdump -s 0 -w /sdcard/packet.pcap\n");
			output.flush();
			
			while(stopFlag){
				Thread.sleep(this.logInterval);
			}

			process.destroy();
			killTcpdump();

		} catch (Exception e) {//open file failed!
			Log.e("Debug",e.getMessage());
		}
	}

	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
