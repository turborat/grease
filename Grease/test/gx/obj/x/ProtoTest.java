package gx.obj.x;

import gx.etc.*;
import gx.io.*;
import gx.test.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class ProtoTest {

	@Test
	public void test1() {
		Proto proto = new Proto();

		proto.b((byte) 123);
		assertEquals(123, proto.b());

		proto.s((short) 456);
		assertEquals(456, proto.s());

		proto.i(789);
		assertEquals(789, proto.i());

		proto.l(Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, proto.l());

		proto.f(Float.MAX_VALUE);
		assertEquals(Float.MAX_VALUE, proto.f(), 0.0001);

		proto.d(Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, proto.d(), 0.0001);
	}

	@Test
	public void testToFrom() {
		Proto p1 = new Proto();
		p1.b((byte) 1);
		p1.s((short) 2);
		p1.i(3);
		p1.l(4);
		p1.f(5f);
		p1.d(6);
		p1.t(7);
		p1.n(Util.id("blah"));

		Buf buf = new Buf();
		p1.to(buf);
		buf.flip();

		Proto p2 = new Proto();
		p2.from(buf);

		Assert.assertEquals(1, p2.b());
		Assert.assertEquals(2, p2.s());
		Assert.assertEquals(3, p2.i());
		Assert.assertEquals(4, p2.l());
		Assert.assertEquals(5, p2.f(), .1);
		Assert.assertEquals(6, p2.d(), .1);
		Assert.assertEquals(7, p2.t());
		Assert.assertEquals("blah", Util.str(p2.n()));
	}

	@Test
	public void testLen() throws Exception {
		Proto proto = new Proto();
		Buf buf = new Buf();
		TestObj.populate(proto);
		proto.to(buf);
		buf.flip();
		Assert.assertEquals(Proto.LEN, buf.limit());
		Assert.assertEquals(Proto.LEN, proto.len());
	}

	@Test
	public void testToString() {
		Proto p = new Proto();
		p.b((byte) 1);
		p.s((short) 2);
		p.i(3);
		p.l(4);
		p.f(5f);
		p.d(6);
		p.t(1414739410314409000L);
		p.n(Util.id("blah"));

		Assert.assertEquals("Proto[b:1 s:2 i:3 l:4 f:5.0 d:6.0 t:07:10:10.314409 n:blah]", p.toString());
	}
}
