package com.Lab973.GreenSmartphone;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LinuxInputEvent {
	private int sec;
	private int usec;
	private short type;
	private short code;
	private int value;

	public static final int SIZE = 16;
	public LinuxInputEvent(byte raw[])
	{
		ByteBuffer b = ByteBuffer.wrap(raw);
		b.order(ByteOrder.LITTLE_ENDIAN);
		sec = b.getInt();
		usec = b.getInt();
		type = b.getShort();
		code = b.getShort();
		value = b.getInt();
	}
	public void changeSec(int diff)
	{
		sec += diff;
	}
	public byte[] getRaw()
	{
		ByteBuffer b = ByteBuffer.allocate(16);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(sec);
		b.putInt(usec);
		b.putShort(type);
		b.putShort(code);
		b.putInt(value);
		return b.array();
	}
	@Override
	public String toString()
	{
		return String.format("EVENT: sec %d usec %d type %04x code %04x value %d", sec, usec, type, code, value);
	}
	
	public String toLogFile()
	{
		return String.format("%d %d %04x %04x %d\r\n", sec, usec, type, code, value);
	}
}
