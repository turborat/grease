package gx.etc;

import java.lang.management.*;
import java.text.*;
import java.util.*;

public class Util {

	private static final DateFormat TIME_FMT = new SimpleDateFormat("HH:mm:ss.");
	private static final Date date = new Date();
	private static long t0;

	public static String elapsed(long t)	{
		if (t0 == 0) t0 = t;
		t -= t0;
		long secs = t / 1000_000_000 ;
		long mics = (t % 1000_000_000) / 1000 ;
		return String.format("%d.%06d", secs, mics);
	}

	public static String prettyTime(long ns) {
		date.setTime(ns/1000_000);
		String ret = TIME_FMT.format(date);
		long ms = (ns % 1000_000_000) / 1000;
		return ret + String.format("%06d", ms);
	}

	public static RuntimeException die(Throwable t) {
		t.printStackTrace();
		System.exit(-1);
		return null;
	}

	public static RuntimeException die(String msg) {
		System.err.println(msg);
		System.exit(-1);
		return null;
	}

	public static boolean isAscii(byte b) {
		return ' ' <= b && b <= '~' ;
	}

	public static int id(String str) {
		int id=0;
		for (int i=0 ; i<4 ; ++i) {
			id <<= 8;
			if (str.length() > i)
				id += str.charAt(i);
		}
		return id;
	}

	public static String str(int n) {
		int len=0;
		byte[] bytes = new byte[4];
		for (int i=0 ; i<4 ; ++i) {
			bytes[i] = (byte) (n >> (3-i)*8);
			if (bytes[i] != 0)
				len = i+1;
		}
		return new String(bytes, 0, len);
	}

	public static String bin(long l) {
		return Long.toBinaryString(l);
	}

	public static short pid() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		return Short.parseShort(name.replaceAll("@.*", ""));
	}
}
