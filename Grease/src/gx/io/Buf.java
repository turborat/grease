package gx.io;

import gx.etc.*;

import java.nio.*;

import static java.lang.String.*;

public class Buf {

	// http://mechanical-sympathy.blogspot.co.uk/2012/07/native-cc-like-performance-for-java.html

	public static final int LEN = 0x100;

	private final ByteBuffer buf ;

	public Buf() {
		this(LEN);
	}

	public Buf(int len) {
		this.buf = ByteBuffer.allocateDirect(len);
	}

	public ByteBuffer buf() {
		return buf;
	}

	public int limit() {
		return buf.limit();
	}

	public void limit(int l) {
		buf.limit(l);
	}

	public int pos() {
		return buf.position();
	}

	public void clear() {
		buf.clear();
	}

	public void flip() {
		buf.flip();
	}

	public void putLong(long l) {
		buf.putLong(l);
	}

	public long getLong(int i) {
		return buf.getLong(i);
	}

	public void putDouble(double d) {
		buf.putDouble(d);
	}

	public double getDouble(int idx) {
		return buf.getDouble(idx);
	}

	public void putFloat(double f) {
		buf.putFloat((float) f);
	}

	public float getFloat(int i) {
		return buf.getFloat(i);
	}

	public void putShort(int word) {
		buf.putShort((short) word);
	}

	public short getShort(int idx) {
		return buf.getShort(idx);
	}

	public void putInt(int i) {
		buf.putInt(i);
	}

	public int getInt(int i) {
		return buf.getInt(i);
	}

	public void putByte(int b) {
		buf.put((byte) b);
	}

	public void putByte(int idx, byte...bytes) {
		buf.put(bytes, idx, bytes.length);
	}

	public byte getByte(int idx) {
		return buf.get(idx);
	}

	@Override
	public String toString() {
		String ret = String.format("Buf[%d]", limit()) ;
		if (limit() > 0) {
			ret += "\n" + picture();
		}
		return ret;
	}

	public String picture() {
		String ret = "";
		String hex = "";
		String chr = "";
		boolean something = false;

		for (int i=0 ; i<buf.limit() ; ++i) {
			byte b = buf.get(i);
			hex += format("%02X", b);
			chr += Util.isAscii(b) ? (char) b : '.' ;
			something |= (b != 0);

			if ((i+1)%4==0) {
				hex += " ";
			}

			if ((i+1)%16==0 || i+1==buf.limit()) {
				int boundary = i;
				while(boundary%16!=0) -- boundary;

				if (something) {
					ret += format("%02d: %-36s %s\n", boundary, hex, chr) ;
					something = false;
				}
				hex = "" ;
				chr = "" ;
			}
		}

		if (hex.length() > 1) {
			ret += "etc..\n" ;
		}

		return ret ;
	}

//	public void copyTo(Buf buf2) {
//		for (int i=0 ; i<len ; ++i) {
//			buf2.putByte(i, getByte(i));
//		}
//		buf2.pos(len);
//	}

}
