package gx.test;

import gx.obj.*;
import gx.obj.exp.ObjRO;

import java.lang.reflect.*;

public class TestObj extends Obj {
	private byte type=-1;

	@Override
	public byte type() {
		return type;
	}

	public void type(byte type) {
		this.type = type;
	}

	@Override
	public int len() {
		return ObjRO.LEN;
	}

	@Override
	public String toString() {
		return "TestObj[]";
	}

	public static void populate(Obj obj) throws Exception {
		for (Method m : obj.getClass().getDeclaredMethods()) {
			Class<?>[] args = m.getParameterTypes();
			if (args.length == 1) {
				if (args[0] == byte.class) {
					m.invoke(obj, (byte) (Math.random()*Byte.MAX_VALUE));
				}
				else if (args[0] == short.class) {
					m.invoke(obj, (short) (Math.random()*Short.MAX_VALUE));
				}
				else if (args[0] == int.class) {
					m.invoke(obj, (int) (Math.random()*Integer.MAX_VALUE));
				}
				else if (args[0] == long.class) {
					m.invoke(obj, (long) (Math.random()*Long.MAX_VALUE));
				}
				else if (args[0] == float.class) {
					m.invoke(obj, (float) (Math.random()*Float.MAX_VALUE));
				}
				else if (args[0] == double.class) {
					m.invoke(obj, Math.random()*Double.MAX_VALUE);
				}
			}
		}
	}
}
