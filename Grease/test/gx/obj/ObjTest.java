package gx.obj;

import gx.io.*;
import gx.obj.exp.ObjRO;
import gx.obj.x.*;
import gx.test.*;
import junit.framework.Assert;
import org.junit.*;
import org.junit.Test;

import java.lang.reflect.*;

import static gx.etc.Util.*;
import static gx.obj.Type.*;
import static gx.test.Hacks.field;

public class ObjTest {

	private Obj o1, o2;
	private Buf buf ;

	@Before
	public void before() {
		o1 = new TestObj();
		o2 = new TestObj();
		buf = new Buf();
	}

	@Test
	public void testToFrom() {
		o1.seqNo(666);
		o1.ts(12121121);
		o1.addr((int) id("blah"));

		o1.to(buf);
		o2.from(buf);

		Assert.assertEquals(666, o2.seqNo());
		Assert.assertEquals(12121121, o2.ts());
		Assert.assertEquals("blah", str(o2.addr()));
	}

	@Test
	public void testLEN() {
		int len = Type.find("Byte").len; // start with type

		for (Field f : Obj.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers())) {
				continue;
			}

			if (f.getType() == boolean.class) {
				continue;
			}

			if (f.getType() == Buf.class) {
				continue;
			}

			len += find(f.getType().getName()).len;
		}

		Assert.assertEquals(len-8/*set*/, ObjRO.LEN);
	}

	@Test
	public void testLen() {
		Obj obj = new TestObj();
		Buf buf = new Buf();
		obj.to(buf);
		buf.flip();
		Assert.assertEquals(ObjRO.LEN, buf.limit());
	}

	@Test
	public void testInfo() {
		Assert.assertEquals("01:00:00.000000 TestObj[] :0", o1.info());

		o1.seqNo(666);
		o1.ts(12121121);
		Assert.assertEquals("01:00:00.012121 TestObj[] :666", o1.info());

		o1.addr((int) id("blah"));
		Assert.assertEquals("01:00:00.012121 TestObj[] :666 ->blah", o1.info());
	}

	@Test
	public void isSet() throws Exception {
		Proto p = new Proto();
		for (int i=0 ; i<64 ; ++i) {
			Assert.assertFalse(p.isSet(i));
		}

		Assert.assertEquals(0b0, (long) field(p, "set"));

		p.seqNo(123);
		Assert.assertEquals(0b1, (long) field(p, "set"));

		p.ts(123);
		Assert.assertEquals(0b11, (long) field(p, "set"));

		p.b((byte) 1);
		Assert.assertEquals(0b10011, (long) field(p, "set"));

		p.f(123);
		Assert.assertEquals(0b100010011, (long) field(p, "set"));

		p.reset();
		Assert.assertEquals(0, (long) field(p, "set"));

	}
}
