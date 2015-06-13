package gx.obj;

import org.junit.*;

import static org.junit.Assert.*;

public class TypeTest {

	@Test
	public void testTypeInfo() {
		assertEquals(8, Type.Long.len);
		assertEquals(4, Type.Int.len);
		assertEquals(2, Type.Short.len);
		assertEquals(1, Type.Byte.len);
		assertEquals(8, Type.Double.len);
		assertEquals(4, Type.Float.len);
		assertEquals(8, Type.TS.len);
	}

	@Test
	public void testFind() {
		assertSame(Type.Int, Type.find("i"));
		assertSame(Type.Int, Type.find("in"));
		assertSame(Type.Int, Type.find("int"));
		assertSame(Type.Double, Type.find("d"));
		assertSame(Type.Double, Type.find("D"));
	}

}
