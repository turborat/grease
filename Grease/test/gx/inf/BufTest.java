package gx.inf;


import gx.io.*;
import org.junit.*;

import static org.junit.Assert.*;

public class BufTest {
	@Test
	public void test1() {
		Buf buf = new Buf() ;
		assertEquals(0x100, buf.limit());
		assertEquals(0x000, buf.pos());

		int idx=0;
		buf.putByte(1);
		assertEquals(idx+=1, buf.pos());
		assertEquals(0x100, buf.limit());

		buf.putShort(2);
		assertEquals(idx += 2, buf.pos());
		assertEquals(0x100, buf.limit());

		buf.putInt(3);
		assertEquals(idx += 4, buf.pos());
		assertEquals(0x100, buf.limit());

		buf.putLong(4);
		assertEquals(idx+=8, buf.pos());
		assertEquals(0x100, buf.limit());

		buf.putFloat(5.5);
		assertEquals(idx+=4, buf.pos());
		assertEquals(0x100, buf.limit());

		buf.putDouble(6.6);
		assertEquals(idx+=8, buf.pos());
		assertEquals(0x100, buf.limit());

		buf.flip();
		assertEquals(0, buf.pos());
		assertEquals(idx, buf.limit());

		idx=0;
		assertEquals(1, buf.getByte(idx));
		assertEquals(2, buf.getShort(idx+=1));
		assertEquals(3, buf.getInt(idx+=2));
		assertEquals(4, buf.getLong(idx+=4));
		assertEquals(5.5, buf.getFloat(idx+=8), .0001);
		assertEquals(6.6, buf.getDouble(idx+=4), .0001);

		assertEquals(0, buf.pos());
		assertEquals(idx+=8, buf.limit());

		buf.flip();
		assertEquals(0, buf.pos());
		assertEquals(0, buf.limit());

		buf.clear();
		assertEquals(0, buf.pos());
		assertEquals(0x100, buf.limit());
	}

//	@Test
//	public void copyTo() {
//		Buf buf1 = new Buf();
//		Buf buf2 = new Buf();
//
//		for (int i=0 ; i<buf1.capacity() ; ++i) {
//			buf1.putByte(i, (byte) i);
//		}
//
//		buf1.pos(buf1.capacity());
//		buf1.copyTo(buf2);
//
//		for (int i=0 ; i<buf1.capacity() ; ++i) {
//			assertEquals(buf1.getByte(i), buf2.getByte(i));
//		}
//	}

	@Test
	public void testToString() {
		Buf buf = new Buf();

		buf.putByte(1);
		buf.putByte(2);
		buf.putByte(0xFF);
		buf.putByte(0xD0);
		buf.putByte(0x0D);
		buf.putByte('w');
		buf.putByte('c');
		buf.putByte('W');
		buf.putByte('C');
		buf.putLong(0xFFFFFFFF);
		buf.putLong(0xABCDEF12);
		assertEquals(25, buf.pos());

//		String expect = "Buf{pos:25 lim:256 max:256}\n" +
//			"00: 01020000 FFD00D00 77635743 00000000  ........wcWC....\n" +
//			"16: FFFFFFFF FFFFFFFF FFFFFFFF ABCDEF12  ................\n" ;
//
//		assertEquals(expect, buf.toString());
	}

	@Test
	public void testWord() {
		Buf buf = new Buf();
		buf.putShort(Short.MAX_VALUE);
		assertEquals(2, buf.pos());
		System.out.println(buf.picture());
		assertEquals(Short.MAX_VALUE, buf.getShort(0));
	}
}
