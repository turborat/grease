package gx.obj;

import gx.obj.exp.ObjRO;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import static gx.etc.Util.*;
import static java.lang.String.*;

public class ObjGen {

	private final Pattern MEM_PAT = Pattern.compile("(\\w+)\\[(.*)\\] *");

	private final String DIR = "gx/obj/";

	private byte curType;

	public ObjGen(String fname) throws IOException {
		BufferedReader r ;
		try {
			 r = new BufferedReader(new FileReader(fname));
		}
		catch (FileNotFoundException e) {
			System.err.println("cwd:" + new File(".").getAbsolutePath());
			throw e;
		}

		DispatcherGenerator dg = new DispatcherGenerator();

		String line;
		while ((line = r.readLine()) != null) {
			Matcher m = MEM_PAT.matcher(line);
			if (m.matches()) {
				ClassDef classDef = new ClassDef(m.group(1), m.group(2));
				dg.classDefs.add(classDef);
				write(DIR + "/x/" + classDef.clz + ".java", classDef.buildObj()) ;
			}
			else {
				System.err.println("unparseable:" + line);
			}
		}

		write(DIR + "x/Dispatcher.java", dg.toString());
	}

	private class MemDef {
		final String name, type;
		final Type ti;
		final int offset, n;

		MemDef(String name, String type, int offset, int n) {
			this.name = name;
			this.type = Type.find(type).internalType;
			this.ti = Type.find(type);
			this.offset = offset;
			this.n = n;
		}
	}

	private class ClassDef {
		final String clz;
		final List<MemDef> mems = new ArrayList();
		final int type = ++curType;
		final int len;

		ClassDef(String clz, String fields) {
			this.clz = clz;
			int offset = ObjRO.LEN;
			int n = 3;

			for (String f : fields.split(" ")) {
				String[] xx = f.split(":");
				mems.add(new MemDef(xx[0], xx[1], offset, ++n));
				offset += Type.find(xx[1]).len;
			}

			if (n > 63) {
				die("oops");
			}

			len = offset;
		}

		public String buildObj() {
			// header(s)
			StringBuilder buf = new StringBuilder();

			addHeader(buf);

			buf.append(format("public final class %s extends Obj {\n\n", clz));

			buf.append(format("  static final byte TYPE = %d ;\n", type));
			buf.append(format("  static final int   LEN = %d ;\n\n", len));

			buf.append("  public interface Recv {\n" +
				"    void receive(" + clz + " " + clz.toLowerCase() + ") ;\n" +
				"  }\n\n");

			addMembers(buf, mems);
			addType(buf);
			addLen(buf);
			addBuf(buf, mems);

			for (MemDef mem : mems) {
				addGetter(buf, mem);
				addSetter(buf, mem);
			}

			addToString(buf, mems, clz);

			// finito
			buf.append("}");

			return buf.toString();
		}
	}

	private void addHeader(StringBuilder buf) {
		buf.append("package gx.obj.x ;\n\n") ;
		buf.append("import gx.io.* ;\n\n") ;
		buf.append("import gx.etc.* ;\n") ;
		buf.append("import gx.obj.* ;\n\n") ;
	}

	private void addType(StringBuilder buf) {
		buf.append("  protected byte type() {\n") ;
		buf.append("    return TYPE;\n") ;
		buf.append("  }\n\n") ;
	}

	private void addLen(StringBuilder buf) {
		buf.append("  public int len() {\n") ;
		buf.append("    return LEN;\n") ;
		buf.append("  }\n\n") ;
	}

	private void addBuf(StringBuilder buf, List<MemDef> mems) {
		buf.append("  public void to(Buf buf) {\n");
		buf.append("    super.to(buf) ;\n");
		for (MemDef m : mems) {
			buf.append(format("    buf.put%s(%s()) ;\n", ucFirst(m.type.toString()), m.name));
		}
		buf.append("  }\n\n");
	}

	private void addMembers(StringBuilder buf, List<MemDef> mems) {
		for (MemDef m : mems) {
			buf.append(format("  private %6s %-10s // %d:%d\n", m.type, m.name+";", m.n, m.offset)) ;
		}
		buf.append("\n") ;
	}

	private void addSetter(StringBuilder buf, MemDef m) {
		buf.append(format("  public %s %s() {\n", m.type, m.name));
		buf.append(format("    if (!isSet(%d)) {\n", m.n));
		buf.append(format("      %s(buf.get%s(%d)) ;\n", m.name, ucFirst(m.type), m.offset));
		buf.append(format("    }\n"));
		buf.append(format("    return %s ;\n", m.name));
		buf.append(format("  }\n\n"));
	}

	private void addGetter(StringBuilder buf, MemDef m) {
		buf.append(format("  public void %s(%s %s) {\n", m.name, m.type, m.name));
		buf.append(format("    this.%s = %s ;\n", m.name, m.name));
		buf.append(format("    set(%d) ;\n", m.n));
		buf.append(format("  }\n\n"));
	}

	private void addToString(StringBuilder buf, List<MemDef> mems, String clz) {
			buf.append("  public String toString() {\n");
			buf.append(format("    return \"%s[\"\n", clz));
			for (int i=0 ; i<mems.size() ; ++i) {
				if (mems.get(i).ti == Type.TS) {
					buf.append(format("      + \"%s:\" + Util.prettyTime(%s())", mems.get(i).name, mems.get(i).name));
				}
				else if (mems.get(i).ti == Type.Name) {
					buf.append(format("      + \"%s:\" + Util.str(%s())", mems.get(i).name, mems.get(i).name));
				}
				else {
					buf.append(format("      + \"%s:\" + %s()", mems.get(i).name, mems.get(i).name));
				}
				if (i+1<mems.size()) {
					buf.append(" + \" \"");
				}
				buf.append("\n");
			}
			buf.append("      + \"]\";\n");
			buf.append("  }\n");
		}

	String ucFirst(String str) {
		return str.replaceFirst(".", str.substring(0, 1).toUpperCase());
	}

	private class DispatcherGenerator {
		List<ClassDef> classDefs = new ArrayList();

		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append("package gx.obj.x ;\n\n");
			buf.append("import gx.io.* ;\n");
			buf.append("import gx.obj.* ;\n\n");
			buf.append("public class Dispatcher {\n\n");

			for (ClassDef classDef : classDefs) {
				buf.append("  private final " + classDef.clz + " " + classDef.clz.toLowerCase() +
					" = new " + classDef.clz + "() ;\n");
			}
			buf.append("\n");

			// receiver lists
			for (ClassDef classDef : classDefs) {
				buf.append("  private " + classDef.clz + ".Recv " +
					classDef.clz.toLowerCase() + "Recv;\n");
			}
			buf.append("  private Obj.Recv all;\n");
			buf.append("\n");

			// dispatch()
			buf.append("  public Obj dispatch(Buf buf) {\n" +
				"    Obj obj;\n" +
				"    byte type = buf.getByte(0);\n\n" +
				"    switch(type) {\n");

			for (ClassDef classDef : classDefs) {
				String lcClz = classDef.clz.toLowerCase() ;
				buf.append("      case " + classDef.clz + ".TYPE: \n");
				buf.append("        obj = " + lcClz + ".from(buf) ;\n");
				buf.append("        process(obj);\n");
				buf.append("        if (" + lcClz + "Recv != null) {\n");
			  buf.append("          " + lcClz + "Recv.receive(" + lcClz + ") ;\n");
				buf.append("        }\n");
				buf.append("        break;\n\n");
			}

			buf.append("      default:\n" +
			       "        throw new java.lang.IllegalArgumentException(\"type:\"+type) ;\n\n" +
				     "    }\n\n" +
				     "    if (all != null) {\n" +
						 "      all.receive(obj) ;\n" +
						 "    }\n\n" +
						 "    return obj;\n" +
						 "  }\n\n");


			// overrideables
			buf.append("  protected void process(Obj obj) {\n");
			buf.append("  }\n\n");

			// registers
			buf.append("  public void subscribe(Object...recvr) {\n");
			buf.append("    for (int i=0 ; i<recvr.length ; ++i) {\n");
			for (ClassDef classDef : classDefs) {
				buf.append("      if (recvr[i] instanceof " + classDef.clz + ".Recv) {\n");
				buf.append("        " + classDef.clz.toLowerCase() + "Recv = (" + classDef.clz + ".Recv) recvr[i];\n");
				buf.append("      }\n");
			}
			buf.append("      if (recvr[i] instanceof Obj.Recv) {\n");
     	buf.append("        all = (Obj.Recv) recvr[i];\n");
   		buf.append("      }\n");
			buf.append("    }\n");
			buf.append("  }\n");

			buf.append("}");

			return buf.toString() ;
		}
	}

	private void write(String fname, String data) throws IOException {
		FileWriter writer = new FileWriter(fname);
		writer.write(data) ;
		writer.close();
		System.out.println("+:" + new File(fname).getAbsoluteFile()) ;
	}

	public static void main(String...args) throws IOException {
		new ObjGen(args[0]);
	}

}
