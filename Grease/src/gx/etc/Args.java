package gx.etc;

import java.util.*;

import static gx.etc.Util.die;

public class Args {

	private static class Arg<T> {
		final String name, desc, type;
		final T def;

		Arg(String name, String desc, String type, T def) {
			this.name = name;
			this.desc = desc;
			this.type = type;
			this.def = def;
		}

		String usage() {
			String ret = String.format("  %-10s %s", id(), desc);
			if (def != null) {
				ret += " (default:" + def + ")";
			}
			return ret;
		}

		String id() {
			return type != null
	    	? name + " <" + type + ">"
				: name;
		}
	}

	private final Map<String,Arg> args = new TreeMap();
	private final List<String> cl;
	private final String main;

	public Args(String main, String...cl) {
		this.cl = new ArrayList(Arrays.asList(cl));
		this.main = main;
	}

	public Args(String...cl) {
		this.cl = new ArrayList(Arrays.asList(cl));
		this.main = this.cl.remove(0);
	}

	public String get(String name, String desc, String def) {
		String ret = find(name, desc, "str", def);
		return ret != null ? ret : def;
	}

	public int get(String name, String desc, int def) {
		String str = find(name, desc, "num", def);
		try {
			return str != null ? Integer.parseInt(str) : def;
		}
		catch (NumberFormatException e) {
			throw die("!:" + str + "!=int");
		}
	}

	public float get(String name, String desc, float def) {
		String str = find(name, desc, "num", def);
		try {
			return str != null ? Float.parseFloat(str) : def;
		}
		catch (NumberFormatException e) {
			throw die("!:" + str + "!=float");
		}
	}

	private String find(String name, String desc, String type, Object def) {
		Arg arg = new Arg(name, desc, type, def);
		args.put(name, arg);
		for (int i=0 ; i<cl.size() ; ++i) {
			if (cl.get(i).equals(name)) {
				cl.remove(i);
				if (i >= cl.size()) {
					die("!:" + name + " requires an arg");
				}
				return cl.remove(i) ;
			}
		}
		return null;
	}

	public boolean has(String name, String desc) {
		Arg arg = new Arg(name, desc, null, null);
		args.put(name, arg);
		for (int i=0 ; i<cl.size() ; ++i) {
			if (cl.get(i).equals(name)) {
				cl.remove(i);
				return true;
			}
		}
		return false;
	}

	public void validate() {
		if (has("-h", "this")) {
			die(help());
		}
		if (!cl.isEmpty()) {
			die("?:" + cl.get(0));
		}
	}

	String help() {
		String help = "\nUsage:\n";
		for (Arg arg : args.values()) {
			help += "     " + arg.usage() + "\n" ;
		}
		return help;
	}

	public String prog() {
		return main;
	}
}
