package gx.test;

import gx.etc.*;

import java.lang.reflect.*;

public class Hacks {

	private static <T> T _field(String name, Object o) {

		Class<?> clz = o.getClass();

		while (clz != Object.class) {
			try {
				Field f = clz.getDeclaredField(name);
				f.setAccessible(true);
				return (T) f.get(o);
			}
			catch (NoSuchFieldException nfe) {
				clz = clz.getSuperclass();
			} catch (IllegalAccessException e) {
				Util.die(e);
			}
		}

		throw new RuntimeException("!:" + name);
	}

	public static <T> T field(Object o, String name) {
		T ret = null;
		for (String field : name.split("\\.")) {
			ret = _field(field, o);
			o = ret;
		}
		return ret;
	}
}
