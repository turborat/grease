package gx.obj;

import java.util.*;

public class Type {

	private static final List<Type> all = new ArrayList<>();

	static Type Double = new Type("Double", java.lang.Double.SIZE);
	static Type Float  = new Type("Float", java.lang.Float.SIZE);
	static Type Long   = new Type("Long", java.lang.Long.SIZE);
	static Type Int    = new Type("Int", Integer.SIZE);
	static Type Short  = new Type("Short", java.lang.Short.SIZE);
	static Type Byte   = new Type("Byte", java.lang.Byte.SIZE);
	static Type TS     = new Type("TS", "Long", java.lang.Long.SIZE);
	static Type Name   = new Type("Name", "int", java.lang.Integer.SIZE);

	final int len;
	final String name;
	final String internalType;

	private Type(String name, int len) {
		this(name, name, len);
	}

	private Type(String name, String internalType, int len) {
		this.name = name;
		this.internalType = internalType.toLowerCase();
		this.len = len/8;
		all.add(this);
	}

	public String toString() {
		return internalType;
	}

	public static Type find(String str) {
		str = str.toLowerCase();
		for (Type ti : all) {
			if (ti.name.toLowerCase().startsWith(str)) {
				return ti;
			}
		}
		throw new IllegalArgumentException(str);
	}
}
