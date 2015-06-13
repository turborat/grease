package gx.pool;

import java.lang.reflect.*;

import static gx.etc.Util.*;

public class Pool<T extends Pooled> {

	private final T[] stack;
	private final Constructor<T> ctor;
	private final Class<T> type;
	private int top = 0;
	private int objs = 0;

	public Pool(Class<T> type) {
		this.stack = (T[]) Array.newInstance(type, 8);
		this.type = type;
		try {
			ctor = type.getDeclaredConstructor();
			ctor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw die(e);
		}
	}

	public T acquire() {
		try {
			T obj;
			if (top==0) {
				if (++objs > 25) {
					die("leak:"+type.getSimpleName());
				}
				obj = ctor.newInstance();
			}
			else {
				obj = stack[--top];
			}
			obj.retain(); // refs++
			return obj;
		} catch (Exception e) {
			throw die(e);
		}
	}

	public void release(T obj) {
		obj.reset(); // refs=0
		stack[top++] = obj;
	}

	public int size() {
		return top;
	}

}
