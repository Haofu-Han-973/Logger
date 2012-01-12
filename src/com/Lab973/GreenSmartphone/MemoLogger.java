package com.Lab973.GreenSmartphone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.text.format.Formatter;
import android.util.Log;
import android.content.Context;

public class MemoLogger extends MyLogger {
	
	protected ActivityManager m_acMger;
	protected MemoryInfo m_mi;
	protected boolean m_bOpenFile=false;
	protected long m_iTotalMemory=0;
	protected CharSequence m_sNotification;
	protected Activity m_a;

	public MemoLogger(String logFileName, int interval, Activity a) {
		super(logFileName, interval, a);
		// TODO Auto-generated constructor stub

		m_a=a;
		m_acMger = (ActivityManager) m_a.getSystemService(Context.ACTIVITY_SERVICE);
		m_mi = new MemoryInfo();
		m_iTotalMemory = getTotalMemory();
	}

	@Override
	public String getLogValue() {
		// TODO Auto-generated method stub
		
		String strLog=getSysTime()+":  "+getMemoryInfo()+"\n";
		
		return strLog;
	}

	
	public String getMemoryInfo()
    {
    	m_acMger.getMemoryInfo(m_mi);
    	
    	float proportion=(float)(m_mi.availMem)/(float)(m_iTotalMemory);
    	int percent=(int)(proportion*100);
    	
    	String rstr=""+percent+"%  "+Formatter.formatFileSize(m_a.getBaseContext(), m_mi.availMem)+"  "+Formatter.formatFileSize(m_a.getBaseContext(), m_iTotalMemory);
    	
    	return rstr;
    }
    
    public String getSysTime()
    {
    	long time=System.currentTimeMillis();
    	Calendar calendar=Calendar.getInstance();
    	calendar.setTimeInMillis(time);
    	String rstr=""+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
    	
    	return rstr;
    }
    
    public long getTotalMemory()
    {
    	String str1 = "/proc/meminfo";// ϵͳ�ڴ���Ϣ�ļ�  
        String str2;  
        String[] arrayOfString;  
        long initial_memory = 0;  
  
        try {  
            FileReader localFileReader = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(  
                    localFileReader, 8192);  
            str2 = localBufferedReader.readLine();// ��ȡmeminfo��һ�У�ϵͳ���ڴ��С  
  
            arrayOfString = str2.split("\\s+");  
            for (String num : arrayOfString) {  
                Log.i(str2, num + "/t");  
            }  
  
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// ���ϵͳ���ڴ棬��λ��KB������1024ת��ΪByte  
            localBufferedReader.close();  
  
        } catch (IOException e) {  
        	e.printStackTrace();
        } 
        
        return initial_memory;
    }
}
