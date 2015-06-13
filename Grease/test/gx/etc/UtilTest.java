package gx.etc;

import gx.io.*;
import org.junit.*;

import static gx.etc.Util.*;

public class UtilTest {

	@Test
	public void testPrettyTime() {
		long now = 1414344404138450666L;
		Assert.assertEquals("17:26:44.138450", Util.prettyTime(now));
	}

	@Test
	public void testElapsed() {
		long now = 1414344404138450666L;
		Assert.assertEquals("0.000000", Util.elapsed(now));
		Assert.assertEquals("1.000000", Util.elapsed(now+1_000_000_000L));
		Assert.assertEquals("1.000001", Util.elapsed(now + 1_000_001_000L));
	}

	@Test
	public void testId() {
		Assert.assertEquals("abcd", str(id("abcd")));
		Assert.assertEquals("'a'", "'"+str(id("a"))+"'");
		Assert.assertEquals(0, id(""));
		Assert.assertEquals("", str(id("")));
		Assert.assertEquals(0, str(id("")).length());
		Assert.assertEquals(1, str(id("x")).length());
		Assert.assertEquals(2, str(id("xx")).length());
		Assert.assertEquals(3, str(id(" xx")).length());
		Assert.assertEquals(" xo ", str(id(" xo ")));

		Buf buf = new Buf();
		buf.putInt(id("nofx"));
		buf.flip();
		Assert.assertEquals("00: 6E6F6678                             nofx\n", buf.picture());
	}

	@Test
	public void testBin() {
		Assert.assertEquals("0", bin(0));
		Assert.assertEquals("10", bin(2));
		Assert.assertEquals("111", bin(7));
		Assert.assertEquals("1111111111111111111111111111111111111111111111111111111111111111", bin(-1));
	}
}
